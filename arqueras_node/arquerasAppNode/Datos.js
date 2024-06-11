var mysql = require('mysql2')

class Datos {
    constructor() {
        this.conexion;
    }

    crearConexion() {

        this.conexion = mysql.createConnection({
            host: "db-mysql",  //Container name
            user: "arqueras",
            password: "arqueras",
            database: "arqueras_db"
        });
        this.conexion.connect(function (err) {
            if (err) {
                console.error('Error de conexion: ' + err.stack);
                return;
            }
            console.log('-| Conexion a la BBDD realizada ');
        });
    };

    crearPool() {
        /*this.conexion = mysql.createPool({
             host : 'localhost',
             database : 'steamestacionmeteo',
             user : 'root',
             password :''
         });*/

        this.conexion = mysql.createPool({
            host: "db-mysql",  //Container name
            user: "arqueras",
            password: "arqueras",
            database: "arqueras_db"
        });
    }

    async insert(sql) {
        await this.conexion.query(sql, function (err, result) {
            if (err) throw err;
            console.log("-| 1 registro insertado");
        });
    }


    async update(sql) {
        await this.conexion.query(sql, (err, result) => {
            if (err) {
                console.log(err)
            }
            //console.log(result.affectedRows + ' registros modificados con la operaciÃ³n '+ sql)
        })
    }


    consulta(sql, callback) {
        this.conexion.query(sql, function (err, result) {
            console.log("-| Realizando consulta a la BBDD")
            if (err) console.log('-| Error en la consulta: ' + err)
            else {
                callback(result);
            }
        })
    }

    selectModulosGrupo(sql, idMatricula, callback) {
        var modulos = []
        this.pool.query(sql, function (err, results) {
            if (err) console.log('-| Error')
            modulos.push(results);
            callback(modulos, idMatricula);
        })
    }
}
module.exports = { Datos };