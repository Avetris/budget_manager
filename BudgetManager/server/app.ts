import * as express from 'express';
import * as path from 'path';  // < -- add this
import { Express, Request, Response } from 'express';

export default function createApp(): Express {
    const app = express();
    const clientDir = path.join(__dirname, '../public');  // <-- add this 
    app.use(express.static(clientDir));                   // <-- and add this
    app.get('/api/:name', async (req: Request, res: Response) => {
        const name = req.params.name;
        const greeting = { greeting: `Hello, ${ name }` };
        res.send(greeting);
    });
    return app;
}