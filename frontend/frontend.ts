import * as express from "express";
import * as path from "path";

const app = express();
app.set('views', path.join(__dirname, '/'));
app.set('view engine', 'pug');

app.use(express.static(path.join(__dirname, 'public')));

app.use(express.json());

const port: number = 3000;
const backendUrl = "http://backend:8080";

app.get('/', (req, res) => {
    res.render('index.pug', { title: "Url Shortener" });
});

app.post('/vip_shorten', async (req, res) => {
    const { longUrl, vipKey, timeToLive, timeToLiveUnit } = req.body;

    try {
        const response = await fetch(`${backendUrl}/make_vip_shorter`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                longUrl,
                vipKey,
                timeToLive,
                timeToLiveUnit,
            }),
        });

        if (!response.ok) {
            const error = await response.json();
            return res.status(response.status).json(error);
        }

        const data = await response.json();
        res.json(data);
    } catch (error) {
        res.status(500).json({ error: "Failed to connect to the backend" });
    }
});

app.post('/common_shorten', async (req, res) => {
    const { longUrl } = req.body;

    try {
        const response = await fetch(`${backendUrl}/make_shorter`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ longUrl }),
        });

        if (!response.ok) {
            const error = await response.json();
            return res.status(response.status).json(error);
        }

        const data = await response.json();
        res.json(data);
    } catch (error) {
        res.status(500).json({ error: "Failed to connect to the backend" });
    }
});

app.delete('/delete/:secretKey', async (req, res) => {
    const { secretKey } = req.params;

    if (!secretKey) {
        return res.status(400).json({ error: "Secret key is required" });
    }

    try {
        const mainServiceResponse = await fetch(`${backendUrl}/admin/${secretKey}`, { method: 'DELETE' });

        if (!mainServiceResponse.ok) {
            console.error('Main service deletion failed');
        }

        res.status(200).json({ message: "Deletion attempted in both services" });
    } catch (error) {
        console.error('Error deleting redirection:', error);
        res.status(500).json({ error: "Failed to delete redirection" });
    }
});


app.listen(port, () => {
    console.log(`Frontend app listening on port ${port}`);
});
