import * as express from "express";
import * as path from "path";

const app = express();
app.set('views', path.join(__dirname, '/'));
app.set('view engine', 'pug');

const port: number = 3000;
const backendUrl = "http://backend:3001";

app.get('/', async (req, res) => {
    const data = "ssd"

    res.render('index.pug', { title: "KIT Frontend", cat_url: data });
});

app.listen(port, () => {
    console.log(`Frontend app listening on port ${port}`)
})
