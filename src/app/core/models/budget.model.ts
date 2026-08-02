import { Client } from "@core/models/client.model"
import { TaskUnit } from "@core/models/task.model"

export interface BudgetTask {
    id?: string;
    taskId?: string;
    title: string;
    description: string;
    price: number;
    unit: TaskUnit;
    count: number;
}

export interface BudgetGroup {
    id?: string;
    name: string;
    tasks: BudgetTask[];
}

export type BudgetStatus = 'draft' | 'sent' | 'accepted' | 'rejected';

export interface Budget {
    id?: string
    number: string;
    client: Client;
    date: string
    status: BudgetStatus;
    groups: BudgetGroup[];
    ungroupedTasks: BudgetTask[];
    images?: string[];
    vatRate: number;
    subtotal: number;
    vatAmount: number;
    total: number;
    companyId?: string;
}