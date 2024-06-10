var express = require('express');
var router = express.Router();
var {Control} = require("../Control");

const controlador = new Control();

/* GET home page. */
router.get('/', function(req, res, next) {
  controlador.testDBQuery((results) => {
    /*if (err) {
      // Maneja el error
      console.error('Error al realizar la consulta:', err);
      return;
    }*/
    // Haz algo con los resultados si es necesario
    //console.log('Resultado de la consulta:', results);
    res.render("index", {
      //datos: results
      datos: results
    });
  });
  //res.render('index', { title: 'Prueba' });
  
});

module.exports = router;
