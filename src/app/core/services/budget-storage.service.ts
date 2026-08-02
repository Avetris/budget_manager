import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { addDoc, collection, Firestore } from '@angular/fire/firestore';
import { DeletedImage } from '@core/models/deleted-images.model';

@Injectable({ providedIn: 'root' })
export class BudgetStorageService {
    private readonly http = inject(HttpClient);
    private readonly firestore = inject(Firestore);

    private readonly cloudName = 'avetrisbm';
    private readonly uploadPreset = 'budget_manager';

    async uploadBudgetImages(companyName: string, budgetNumber: string, files: File[]): Promise<string[]> {
        const sanitizedCompanyName = companyName.toLowerCase().replace(/[^a-z0-9]/g, '-');
        const sanitizedBudgetNumber = budgetNumber.toLowerCase().replace(/[^a-z0-9]/g, '-');
        const targetFolder = `budget_manager/${sanitizedCompanyName}/${sanitizedBudgetNumber}`;

        const uploadPromises = files.map(async (file) => {
            const formData = new FormData();
            formData.append('file', file);
            formData.append('upload_preset', this.uploadPreset);

            formData.append('asset_folder', targetFolder);

            const response = await firstValueFrom(
                this.http.post<{ secure_url: string }>(
                    `https://api.cloudinary.com/v1_1/${this.cloudName}/image/upload`,
                    formData
                )
            );

            return response.secure_url;
        });

        return Promise.all(uploadPromises);
    }

    getPublicIdFromUrl(url: string): string | undefined {
        try {
            const regex = /\/v\d+\/(.+)\.[a-z]+$/i;
            const match = url.match(regex);
            return match ? match[1] : undefined;
        } catch {
            return undefined;
        }
    }

    async trackDeletedImages(urls: string[], companyId: string, budgetId: string): Promise<void> {
        if (!urls.length) return;

        const deletedCollection = collection(this.firestore, 'deleted_images');

        const promises = urls.map(url => {
            const record: DeletedImage = {
                url,
                publicId: this.getPublicIdFromUrl(url),
                companyId,
                budgetId,
                deletedAt: new Date().toISOString()
            };
            return addDoc(deletedCollection, record);
        });

        await Promise.all(promises);
    }
}