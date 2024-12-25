import * as express from 'express';
import * as path from "path";

// @ts-ignore
const app = express();
const port: number = 3000;
const backendUrl = "http://backend:3001";

// Настройка для работы с EJS (или можно использовать Pug/Jade)
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// Обслуживание статических файлов (CSS, JS)
app.use(express.static(path.join(__dirname, 'public')));

// Главная страница с кнопками
app.get('/', async (req, res) => {
    const cat_data = await fetch(`${backendUrl}/cat_get`);
    const data = await cat_data.text();

    res.render('index', { title: "URL Shortener", cat_url: data });
});

// Обработчик для создания VIP-ссылки
app.post('/create-vip-link', async (req, res) => {
    // Здесь должна быть логика для создания VIP-ссылки через backend
    const originalUrl = req.body.url;
    const shortUrl = await createShortLink(originalUrl, 'vip'); // Примерный запрос на создание короткой ссылки
    res.json({ shortUrl });
});

// Обработчик для создания обычной ссылки
app.post('/create-regular-link', async (req, res) => {
    const originalUrl = req.body.url;
    const shortUrl = await createShortLink(originalUrl, 'regular');
    res.json({ shortUrl });
});

// Пример функции для создания короткой ссылки
async function createShortLink(originalUrl: string, type: 'vip' | 'regular') {
    // Имитация создания короткой ссылки
    return `short.ly/${type}-${originalUrl.slice(-6)}`; // Например, генерируем короткую ссылку
}

app.listen(port, () => {
    console.log(`Frontend app listening on port ${port}`);
});
