import 'dart:async';
import 'dart:convert';
import '../../models/agencia.dart';
import '../../services/connection.dart';

// GET getAllCategorias
// POST createCategoria
// PUT updateCategoria
const API_AGENCIAS = "agencias";

// GET getCategoria
// DELETE deleteCategoria
const API_AGENCIA = "agencias/";
const API_AGENCIA_TEST = "?id.greaterThanOrEqual=5";

Future<Agencia> agencia(String id) async {
    var response = await restGet(API_AGENCIA + id,true,false);
    return Agencia.fromJson(json.decode(response));
}


//
Future<List<Agencia>> agencias() async {
    print("get agencias");

    var data = await restGet(API_AGENCIA  + API_AGENCIA_TEST,true,false);

    return agenciaData(data);
}

List<Agencia> agenciaData(String data) {
    final parsed =json.decode(data).cast<Map<String, dynamic>>();
    print(parsed.toString());
    List<Agencia> lu= parsed.map<Agencia>((json) => Agencia.fromJson(json)).toList();

    print("QUantidade de agencias no metodo agenciaData ===>>>> " + lu.length.toString());
    return lu;
}
