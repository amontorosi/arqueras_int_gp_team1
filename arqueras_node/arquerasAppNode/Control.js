const {Datos} = require('./Datos');

class Control{

    constructor (){
        this.basedatos = new Datos();
        this.basedatos.crearConexion();
    }

    testDBQuery(callback) {
        var query1=`select * from scores order by score desc`
        this.basedatos.consulta(query1, (resultado) => {
            if (resultado.length==0){
                console.log("No hay datos");
                callback('0')
            }else{
                var datos=JSON.stringify(resultado)
                console.log('Consulta realizada!');
                callback(resultado);
            }
        });
    }

    recogerDatosMeteriologicos(callback) {
        var query1=`select * from datos1 order by fechaSistema desc`
        this.basedatos.consulta(query1, (resultado) => {
            if (resultado.length==0){
                console.log("No hay datos");
                callback('0')
            }else{
                var datos=JSON.stringify(resultado)
                console.log('Consulta realizada!');
                callback(datos)
            }
        });
    }

    recogerDatosPrecipitaciones(callback) {

        var query1=`select fecha, mmacumulado "mma" from precipitacion order by fecha desc`
        this.basedatos.consulta(query1, (resultado) => {
            if (resultado.length==0){
                console.log("No hay datos");
                callback('0')
            }else{
                var datos=JSON.stringify(resultado);
                console.log('Consulta realizada!');
                callback(datos)
            }
        });
    }

    //Se crea un objeto con los datos de la tabla datos1 filtrando por que sean del mismo dia
    recogerDatosMeteriologicosDiarios(callback) {
        var query1=`select * from datos1 where fechaSistema>sysdate()-00000001000000 and fechaSistema<sysdate()`;
        this.basedatos.consulta(query1, (resultado) => {
            if (resultado.length==0){
                console.log("-| No hay datos");
                callback('0')
            }else{
                var datos=JSON.stringify(resultado);
                console.log('Consulta realizada!');
                callback(datos)
            }
        });
    }

    recogerDatosMeteriologicosMensuales(callback) {

        var query1=`SELECT fechaSistema, MAX(temperatura) maxT, MIN(temperatura) minT, MAX(presion)maxP, MIN(presion) minP, MAX(humedad)maxH, MIN(humedad) minH, MAX(intUltravioleta)maxU, MIN(intUltravioleta) minU FROM datos1 GROUP by EXTRACT(DAY FROM fechaSistema), EXTRACT(MONTH FROM fechaSistema), EXTRACT(YEAR FROM fechaSistema)`
        this.basedatos.consulta(query1, (resultado) => {
            if (resultado.length==0){
                console.log("No hay datos");
                callback('0')
            }else{
                var datos=JSON.stringify(resultado);
                console.log('Consulta realizada!');
                callback(datos)
            }
        });
    }

    //Se crea un objeto con los datos de la tabla precipitaciones filtrando por que sean del mismo dia
    recogerDatosPrecipitacionesDiarias(callback) {

        var query1=`select fecha, mmacumulado "mma" from precipitacion where fecha>sysdate()-00000001000000 and fecha<sysdate()`;
        this.basedatos.consulta(query1, (resultado) => {
            if (resultado.length==0){
                console.log("No hay datos");
                callback('0')
            }else{
                var datos=JSON.stringify(resultado);
                console.log('Consulta realizada!');
                callback(datos)
            }
        });
    }

    recogerDatosPrecipitacionesMensuales(callback) {

        var query1=`select fecha, sum(mmacumulado) "mmaT" from precipitacion GROUP BY EXTRACT(DAY FROM fecha), EXTRACT(MONTH FROM fecha), EXTRACT(YEAR FROM fecha)`
        this.basedatos.consulta(query1, (resultado) => {
            if (resultado.length==0){
                console.log("No hay datos");
                callback('0')
            }else{
                var datos=JSON.stringify(resultado);
                console.log('Consulta realizada!');
                callback(datos)
            }
        });
    }

}
module.exports={Control};