// Test db.js
const mysql = require("mysql2");

const connection = mysql.createConnection({
    host: "db-mysql",  //Container name
    user: "arqueras",
    password: "arqueras",
    database: "arqueras_db",
});

function testDBQuery(callback) {
    connection.query("SELECT * FROM table1", (err, results, fields) => {
        if (err) {
            console.error("Error al ejecutar la consulta:", err.stack);
            callback(err, null);
            return;
        }
        console.log("Resultado de la consulta:", results);
        callback(null, results);
    });
}

module.exports = {
    connection,
    testDBQuery,
};
