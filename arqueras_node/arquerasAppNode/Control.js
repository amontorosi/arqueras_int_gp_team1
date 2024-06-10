const { Datos } = require('./Datos');

class Control {

    constructor() {
        this.basedatos = new Datos();
        this.basedatos.crearConexion();
    }

    scoreQuery(callback) {
        var query1 = `select * from scores order by score desc`
        this.basedatos.consulta(query1, (resultado) => {
            if (resultado.length == 0) {
                console.log("No hay datos");
                callback('0')
            } else {
                //var datos=JSON.stringify(resultado)
                console.log('Consulta realizada!');
                console.log(resultado);
                callback(resultado);
            }
        });
    }
}
module.exports = { Control };