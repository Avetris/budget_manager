export interface Client {
    id?: string
    nif: string
    isCompany: boolean
    name: string
    address?: string
    phone?: string
    email?: string
    companyId?: string
}