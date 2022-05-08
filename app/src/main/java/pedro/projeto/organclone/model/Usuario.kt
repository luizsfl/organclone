package pedro.projeto.organclone.model

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import pedro.projeto.organclone.config.ConfiguracaoFirebase

class Usuario {
    lateinit var idUsuario: String
    lateinit var nome: String
    lateinit var email: String
    lateinit var senha: String
    var receitaTotal: Double = 0.0
    var despesaTotal: Double = 0.0




    constructor(nome: String, email: String,senha : String, idUsuario :String) {
        this.nome = nome
        this.email = email
        this.senha = senha
        this.idUsuario = idUsuario
    }

    constructor() {
    }



    fun salvar() {
            var firebase:DatabaseReference = ConfiguracaoFirebase.getFirebaseDataBse()

            firebase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this)


        }



}