package pedro.projeto.organclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import pedro.projeto.organclone.R
import pedro.projeto.organclone.config.ConfiguracaoFirebase

class MainActivity :AppCompatActivity(){

    private lateinit var autenticacao: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)


    }


    fun btEntrar(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun btCadastrar(view: View){
        startActivity(Intent(this, CadastroActivity::class.java))

    }


}