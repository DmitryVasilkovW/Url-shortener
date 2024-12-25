import * as express from "express";
import * as path from "path";

const app = express();
app.set('views', path.join(__dirname, '/'));
app.set('view engine', 'pug');

// Настройка для отдачи статики (CSS, JS и другие файлы)
app.use(express.static(path.join(__dirname, 'public')));

const port: number = 3000;

app.get('/', async (req, res) => {
    res.render('index.pug', { title: "Url shortener" });
});

app.listen(port, () => {
    console.log(`Frontend app listening on port ${port}`);
});
