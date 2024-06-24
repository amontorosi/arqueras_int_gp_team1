function formatDate(d) {
    var meses = ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"];
    return d.getDate() + " de " + meses[d.getMonth()] + " de " + d.getFullYear() + " (" + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds() + ")";
}

function formatDiff(d) {
    var diffSpanish;
    switch (d) {
        case "easy":
            diffSpanish = "Fácil";
            break;
        case "normal":
            diffSpanish = "Normal";
            break;
        case "hard":
            diffSpanish = "Difícil";
            break;
    }
    return diffSpanish;
}

module.exports = {
    formatDate: formatDate,
    formatDiff: formatDiff
}