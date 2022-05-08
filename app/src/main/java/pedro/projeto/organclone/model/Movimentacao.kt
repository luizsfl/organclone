package pedro.projeto.organclone.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import pedro.projeto.organclone.config.ConfiguracaoFirebase
import pedro.projeto.organclone.helper.Base64Custom
import pedro.projeto.organclone.helper.DateCustom

class Movimentacao {
    var data : String = ""
    var categoria : String = ""
    var descricao : String = ""
    var tipo : String = ""
    var valor: Double = 0.0


    fun salvar(dataEscolhida :String){

        var autenticacao : FirebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao()
        var email:String? = autenticacao.currentUser?.email
        var idUsuario:String = Base64Custom.codificarBase64(email.toString())

        var mesAno = DateCustom.mesAnoDataEscolhida(dataEscolhida)

       var firebase: DatabaseReference = ConfiguracaoFirebase.getFirebaseDataBse()
        firebase.child("movimentacao")
            .child(idUsuario)
            .child(mesAno)
            .push()
            .setValue(this)


    }


    }