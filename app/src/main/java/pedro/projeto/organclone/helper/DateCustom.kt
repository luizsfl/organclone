package pedro.projeto.organclone.helper

import android.util.Log
import java.text.SimpleDateFormat

class DateCustom {
    companion object {
        fun dataAtual(): String {
            var data: Long = System.currentTimeMillis()
            var simleDateFormate: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            var dataString = simleDateFormate.format(data)

            return dataString
        }

        fun mesAnoDataEscolhida(dataEscolhida:String): String{

            var retornoData = dataEscolhida.split("/")
            var dia = retornoData[0]
            var mes = retornoData[1]
            var ano = retornoData[2]
            var mesAno = mes+ano

            return mesAno

        }



    }


}