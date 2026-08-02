export type TaskUnit = 'ud' | 'h' | 'm2' | 'm' | 'kg' | 'global';

export interface Task {
    id?: string
    title: string
    description: string
    price: number
    unit?: TaskUnit
    count?: number
    companyId?: string
}