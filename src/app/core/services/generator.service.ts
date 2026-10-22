import { inject, Injectable } from '@angular/core';
import { Budget, BudgetTask } from '@core/models/budget.model';
import { Client } from '@core/models/client.model';
import { Company } from '@core/models/company.model';
import { TranslocoService } from '@jsverse/transloco';

import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';
import { Content, TableCell, TDocumentDefinitions } from 'pdfmake/interfaces';


pdfMake.addVirtualFileSystem(pdfFonts);

@Injectable({ providedIn: 'root' })
export class GeneratorService {

    readonly translocoService = inject(TranslocoService)

    async generateBudgetPdf(budget: Budget, company: Company): Promise<void> {
        const iconBase64 = await this.getBase64ImageFromUrl(company.icon)
        const images = [iconBase64, iconBase64, iconBase64, iconBase64, iconBase64] //budget.images ? await this.getImages(budget.images) : []

        console.log(this.translocoService.translate("pdf.num_budget"))
        const docDefinition: TDocumentDefinitions = {
            pageSize: 'A4',
            pageMargins: [30, 20, 30, 40],

            defaultStyle: {
                font: 'Roboto',
                fontSize: 9,
                color: '#1e293b',
                lineHeight: 1.3
            },


            footer: (currentPage: number, pageCount: number): Content => {
                return {
                    margin: [40, 10, 40, 0] as [number, number, number, number],
                    columns: [
                        {
                            text: `${company.name} — ${this.translocoService.translate("pdf.num_budget")} ${budget.number}`,
                            fontSize: 8,
                            color: '#64748b',
                            alignment: 'left'
                        },
                        {
                            text: `${this.translocoService.translate("pdf.page", { currentPage: currentPage, pageCount: pageCount })}`,
                            fontSize: 8,
                            color: '#64748b',
                            alignment: 'right'
                        }
                    ]
                };
            },

            styles: {
                headerTitle: { fontSize: 11, bold: true },
                sectionTitle: { fontSize: 10, bold: true, margin: [0, 0, 0, 14] },
                tableHeader: { bold: true, alignment: 'center', fillColor: '#f8fafc' },
                rightAlign: { alignment: 'right' },
                centerAlign: { alignment: 'center' }
            },

            content: [
                this.buildHeader(company, iconBase64),
                this.buildClientAndBudgetInfo(budget, budget.client),
                this.buildTasksTable(budget),
                this.buildTotalsTable(budget),

                ...(company.conditions ? [
                    { text: '', pageBreak: 'before' as const },
                    this.buildConditionsSection(company.conditions)
                ] : []),

                ...(company.garanty ? [
                    { text: '', pageBreak: 'before' as const },
                    this.buildWarrantySection(company.garanty)
                ] : []),

                ...(images.length > 0 ? [
                    { text: '', pageBreak: 'before' as const },
                    this.buildImagesGridSection(images)
                ] : [])
            ]
        };

        pdfMake.createPdf(docDefinition).open();
        //pdfMake.createPdf(docDefinition).download(`${budget.number}.pdf`);
    }

    private buildHeader(company: Company, logoBase64: string): Content {
        const activitiesList: Content[] = (company.header || []).map(activity => ({
            text: activity.toUpperCase(),
            fontSize: 7,
            bold: true,
            alignment: 'center',
            margin: [0, 1, 0, 1] as [number, number, number, number]
        }));

        const leftStack: Content[] = [];
        if (logoBase64) {
            leftStack.push({
                image: logoBase64,
                width: 70,
                margin: [0, 0, 0, 6] as [number, number, number, number]
            });
        }
        leftStack.push(
            { text: company.name, style: 'headerTitle' },
            { text: `${this.translocoService.translate("pdf.nif")}: ${company.nif || '-'}` },
            { text: company.street || '' },
            { text: company.city || '' },
            { text: company.phone ? `${this.translocoService.translate("pdf.phone")}: ${company.phone}` : '' }
        );
        let rightColumn: Content = { text: '' };
        if (activitiesList.length > 0) {
            const cellContent: TableCell = {
                stack: activitiesList,
                margin: [6, 6, 6, 6] as [number, number, number, number]
            };

            rightColumn = {
                table: {
                    widths: ['*'],
                    body: [[cellContent]]
                },
                layout: {
                    hLineWidth: () => 0.5,
                    vLineWidth: () => 0.5,
                    hLineColor: () => '#cbd5e1',
                    vLineColor: () => '#cbd5e1'
                }
            };
        }
        return {
            margin: [0, 0, 0, 20] as [number, number, number, number],
            columns: [
                {
                    width: '60%',
                    stack: leftStack
                },
                {
                    width: '40%',
                    margin: [10, 0, 0, 0] as [number, number, number, number],
                    stack: [rightColumn]
                }
            ]
        };
    }

    private buildClientAndBudgetInfo(budget: Budget, client: Client): Content {
        var clientStacks: Content[] = [
            { text: client.name, bold: true },
            { text: `${client.nif ? (this.translocoService.translate(`pdf.${client.isCompany ? 'nif' : 'dni'}`) + " " + client.nif) : ''}`, bold: true },
        ]
        if (client.address) {
            clientStacks.push({ text: client.address || '' })
        }
        if (client.address) {
            clientStacks.push({ text: `${this.translocoService.translate("pdf.phone")}: ${client.phone || ''} ` })
        }
        if (client.address) {
            clientStacks.push({ text: `${this.translocoService.translate("pdf.email")}: ${client.email || ''} `, color: '#475569' });
        }
        return {
            margin: [0, 0, 0, 20] as [number, number, number, number],
            table: {
                widths: ['50%', '50%'],
                body: [
                    [
                        {
                            stack: [
                                { text: `Nº PRESUPUESTO: ${budget.number} `, bold: true },
                                { text: `FECHA: ${new Date(budget.date).toLocaleDateString('es-ES', { year: "numeric", month: "2-digit", day: "2-digit" })} ` }
                            ],
                            verticalAlignment: 'bottom'
                        },
                        {
                            stack: clientStacks
                        }
                    ]
                ]
            },
            layout: 'noBorders'
        };
    }

    private buildTasksTable(budget: Budget): Content {
        const tableBody: TableCell[][] = [
            [
                { text: `${this.translocoService.translate("pdf.description")}`, style: 'tableHeader' },
                { text: `${this.translocoService.translate("pdf.quantity")}`, style: 'tableHeader' },
                { text: `${this.translocoService.translate("pdf.price")}`, style: 'tableHeader' },
                { text: `${this.translocoService.translate("pdf.total")}`, style: 'tableHeader' }
            ]
        ];

        if (budget.groups && budget.groups.length > 0) {
            budget.groups.forEach(group => {
                const activeGroupTasks = (group.tasks || []).filter(t => t.count > 0);

                // Solo mostramos el grupo si tiene al menos una tarea con cantidad > 0
                if (activeGroupTasks.length > 0) {
                    // Fila de encabezado de grupo (ocupa las 4 columnas con colSpan)
                    tableBody.push([
                        {
                            text: group.name.toUpperCase(),
                            bold: true,
                            fontSize: 9,
                            fillColor: '#f1f5f9', // Color de fondo gris claro para diferenciar el grupo
                            colSpan: 4,
                            margin: [2, 4, 2, 4] as [number, number, number, number]
                        },
                        {}, {}, {} // Celdas vacías requeridas por pdfmake al usar colSpan: 4
                    ]);

                    // Insertar las tareas pertenecientes al grupo
                    activeGroupTasks.forEach(task => {
                        tableBody.push(this.buildTaskRow(task, true)); // true aplica un sangrado suave
                    });
                    const groupTotal = activeGroupTasks.reduce((acc, item) => acc + (item.price * item.count), 0);

                    tableBody.push([
                        {
                            text: `${this.translocoService.translate("pdf.subtotal")} ${group.name}: `,
                            bold: true,
                            alignment: 'right',
                            fontSize: 8,
                            color: '#475569',
                            colSpan: 3,
                            margin: [0, 2, 4, 4] as [number, number, number, number]
                        },
                        {}, {},
                        {
                            text: this.formatCurrency(groupTotal),
                            bold: true,
                            alignment: 'right',
                            fontSize: 8,
                            color: '#0f172a',
                            margin: [0, 2, 0, 4] as [number, number, number, number]
                        }
                    ]);
                }
            });


            if (budget.ungroupedTasks && budget.ungroupedTasks.length > 0) {
                const activeUngrouped = budget.ungroupedTasks.filter(t => t.count > 0);
                if (activeUngrouped.length > 0) {
                    /*if (budget.groups && budget.groups.length > 0) {
                        tableBody.push([
                            {
                                text: 'OTRAS TAREAS',
                                bold: true,
                                fontSize: 9,
                                fillColor: '#f8fafc',
                                colSpan: 4,
                                margin: [2, 4, 2, 4] as [number, number, number, number]
                            },
                            {}, {}, {}
                        ]);
                    }*/

                    activeUngrouped.forEach(task => {
                        tableBody.push(this.buildTaskRow(task, false));
                    });
                }
            }
        }

        return {
            margin: [0, 0, 0, 20] as [number, number, number, number],
            table: {
                headerRows: 1,
                widths: ['*', 35, 55, 60],
                body: tableBody
            },
            layout: 'lightHorizontalLines'
        };
    }

    private buildTaskRow(task: BudgetTask, isGrouped: boolean = false): TableCell[] {
        return [
            {
                stack: [
                    { text: task.title, bold: true, decoration: 'underline' },
                    ...(task.description ? [{ text: task.description, margin: [0, 2, 0, 0] as [number, number, number, number] }] : [])
                ],
                margin: [isGrouped ? 10 : 0, 0, 0, 0] as [number, number, number, number]
            },
            { text: `${task.count} ${task.unit || `${this.translocoService.translate("pdf.ud")}`} `, style: 'centerAlign' },
            { text: this.formatCurrency(task.price), style: 'rightAlign' },
            { text: this.formatCurrency(task.price * task.count), style: 'rightAlign' }
        ];
    }

    private buildTotalsTable(budget: Budget): Content {
        return {
            columns: [
                { width: '*', text: '' }, // Espacio a la izquierda
                {
                    width: 200,
                    table: {
                        widths: ['*', '*'],
                        body: [
                            [
                                { text: `${this.translocoService.translate("pdf.price_base")}:`, bold: true, alignment: 'right' },
                                { text: this.formatCurrency(budget.subtotal), alignment: 'right' }
                            ],
                            [
                                { text: `${this.translocoService.translate("pdf.iva", { iva: budget.vatRate })}: `, bold: true, alignment: 'right' },
                                { text: this.formatCurrency(budget.vatAmount), alignment: 'right' }
                            ],
                            [
                                { text: `${this.translocoService.translate("pdf.price_total")}:`, bold: true, alignment: 'right', fontSize: 11 },
                                { text: this.formatCurrency(budget.total), bold: true, alignment: 'right', fontSize: 11 }
                            ]
                        ]
                    },
                    layout: 'noBorders'
                }
            ]
        };
    }

    private buildConditionsSection(conditions: string): Content {
        const paragraphs = conditions
            .split('\n')
            .filter(p => p.trim().length > 0)
            .map(p => ({ text: p, margin: [0, 0, 0, 8] as [number, number, number, number] }));
        return {
            stack: [
                { text: `${this.translocoService.translate("pdf.conditions")}:`, style: 'sectionTitle' },
                ...paragraphs
            ]
        };
    }

    private buildWarrantySection(warranty: string): Content {
        const paragraphs = warranty
            .split('\n')
            .filter(p => p.trim().length > 0)
            .map(p => ({ text: p, margin: [0, 0, 0, 8] as [number, number, number, number] }));
        return {
            stack: [
                { text: `${this.translocoService.translate("pdf.warranty")}:`, style: 'sectionTitle' },
                ...paragraphs
            ]
        };
    }

    private buildImagesGridSection(imagesBase64: string[]): Content[] {
        if (!imagesBase64 || imagesBase64.length === 0) return [];

        const rows: TableCell[][] = [];

        for (let i = 0; i < imagesBase64.length; i += 2) {
            const row: TableCell[] = [
                {
                    image: imagesBase64[i],
                    fit: [240, 200] as [number, number],
                    alignment: 'center',
                    margin: [0, 5, 0, 5] as [number, number, number, number]
                }
            ];

            if (imagesBase64[i + 1]) {
                row.push({
                    image: imagesBase64[i + 1],
                    fit: [240, 200] as [number, number],
                    alignment: 'center',
                    margin: [0, 5, 0, 5] as [number, number, number, number]
                });
            } else {
                row.push({ text: '' });
            }

            rows.push(row);
        }

        return [
            { text: `${this.translocoService.translate("pdf.annex")}:`, style: 'sectionTitle' },
            {
                table: {
                    widths: ['50%', '50%'],
                    body: rows
                },
                layout: 'noBorders'
            }
        ];
    }

    private formatCurrency(amount: number): string {
        return new Intl.NumberFormat('es-ES', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
            style: 'currency',
            useGrouping: true,
            currency: 'EUR'
        }).format(amount || 0);
    }

    private async getImages(imagePaths: string[]): Promise<string[]> {
        return Promise.all(imagePaths!.map(img => this.getBase64ImageFromUrl(img)))
    }

    private getBase64ImageFromUrl(imageUrl: string): Promise<string> {
        return new Promise((resolve, reject) => {
            const img = new Image();
            img.crossOrigin = 'Anonymous'; // Necesario para imágenes de fuentes externas como Cloudinary
            img.src = imageUrl;
            img.onload = () => {
                const canvas = document.createElement('canvas');
                canvas.width = img.width;
                canvas.height = img.height;
                const ctx = canvas.getContext('2d');
                ctx?.drawImage(img, 0, 0);
                resolve(canvas.toDataURL('image/png'));
            };
            img.onerror = (error) => reject(error);
        });
    }
}

