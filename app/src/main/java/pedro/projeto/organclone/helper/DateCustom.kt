package pedro.projeto.organclone.helper

import java.text.SimpleDateFormat

class DateCustom {
    companion object {
        fun dataAtual(): String {
            var data: Long = System.currentTimeMillis()
            var simleDateFormate: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            var dataString = simleDateFormate.format(data)

            return dataString
        }
    }

}