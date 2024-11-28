import unittest
from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy

# Configuración básica de Flask y SQLAlchemy
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///:memory:'  # Base de datos en memoria para pruebas
db = SQLAlchemy(app)

class Producto(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(80), nullable=False)
    precio = db.Column(db.Float, nullable=False)

@app.route('/api/productos', methods=['POST'])
def crear_producto():
    data = request.get_json()
    nuevo_producto = Producto(nombre=data['nombre'], precio=data['precio'])
    db.session.add(nuevo_producto)
    db.session.commit()
    return jsonify({'id': nuevo_producto.id}), 201

class TestAPI(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()
        with app.app_context():  # Establecer el contexto de la aplicación
            db.create_all()  # Crear las tablas en la base de datos en memoria

    def tearDown(self):
        with app.app_context():  # Establecer el contexto de la aplicación
            db.session.remove()
            db.drop_all()  # Eliminar las tablas después de las pruebas

    def test_crear_producto_api(self):
        response = self.app.post('/api/productos', json={'nombre': 'Mouse', 'precio': 50})
        self.assertEqual(response.status_code, 201)
        
        with app.app_context():  # Establecer el contexto de la aplicación
            producto = Producto.query.filter_by(nombre='Mouse').first()
            self.assertIsNotNone(producto)

if __name__ == '__main__':
    unittest.main()