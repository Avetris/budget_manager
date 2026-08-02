export interface DeletedImage {
    id?: string;
    url: string;
    publicId?: string;
    companyId: string;
    budgetId: string;
    deletedAt: string; // ISO Date
}