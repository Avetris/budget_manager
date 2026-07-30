import { Client } from "@core/models/client.model"
import { Task } from "@core/models/task.model"

export interface Budget {
    id: string
    budgetId: string
    date: Date
    project: string
    client: Client
    vat: number
    tasks: Task[]
}