import { Client } from "./client"
import { Task } from "./task"

export interface Budget {
    id: string
    budgetId: string
    date: Date
    project: string
    client: Client
    vat: number
    tasks: Task[]
}