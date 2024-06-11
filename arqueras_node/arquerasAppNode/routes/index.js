var express = require('express');
var router = express.Router();
var { Control } = require("../Control");
var scripts = require('../public/js/scripts');

const controlador = new Control();

/* GET home page. */
router.get('/', function (req, res, next) {
  controlador.scoreQuery((results) => {
    /*if (err) {
      // Maneja el error
      console.error('Error al realizar la consulta:', err);
      return;
    }*/
    // Haz algo con los resultados si es necesario
    //console.log('Resultado de la consulta:', results);
    res.render("index", {
      //datos: results
      datos: results,
      helper: scripts
    });
  });
  //res.render('index', { title: 'Prueba' });

});

module.exports = router;
