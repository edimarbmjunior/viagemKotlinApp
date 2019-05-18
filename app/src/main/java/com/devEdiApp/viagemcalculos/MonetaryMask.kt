package com.devEdiApp.viagemcalculos

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.*
import kotlin.String as String1


class Mask{

    companion object {
        private fun replaceChars(cpfFull: String1): String1 {
            return cpfFull.replace(".", "").replace("-", "")
                    .replace("(", "").replace(")", "")
                    .replace("/", "").replace(" ", "")
                    .replace("*", "")
        }

        open fun replaceEspecialMonetario(str: String1): String1 {
            return str.replace(".", "").replace(",", "")
                    .replace("R", "").replace("$", "")
        }

        open fun montaValor(vlr:Double, locale: Locale, divisao: Int) : String1{
            if(divisao!=999999999){
                return NumberFormat.getCurrencyInstance(locale).format((vlr/divisao))
            }else{
                return NumberFormat.getCurrencyInstance(locale).format((vlr)).toString()
            }
        }

        fun maskMonetary(valor: EditText, localeBR: Locale) : TextWatcher{
            var current = ""
            val textWatcher: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(!s.toString().equals(current)){
                        valor.removeTextChangedListener(this);

                        var cleanString : String1 = replaceEspecialMonetario(s.toString())

                        val parsed : Double = cleanString.toDouble()
                        /*val localeBR = Locale("pt", "BR")*/
                        val formatted : String1 = montaValor(parsed, localeBR, 100)

                        current = formatted
                        valor.setText(formatted)
                        valor.setSelection(formatted.length)
                        valor.addTextChangedListener(this)
                    }else{
                        s.removeRange(s.lastIndex-1, s.lastIndex)
                    }
                }

                override fun afterTextChanged(s: Editable) {

                }
            }
            return textWatcher
        }

        fun maskFix(mask: String1, valor: EditText): TextWatcher {

            val textWatcher: TextWatcher = object : TextWatcher {
                var isUpdating: Boolean = false
                var oldString: String1 = ""
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val str = replaceChars(s.toString())
                    var vlrWithMask = ""

                    if (count == 0)//is deleting
                        isUpdating = true

                    if (isUpdating) {
                        oldString = str
                        isUpdating = false
                        return
                    }

                    var i = 0
                    for (m: Char in mask.toCharArray()) {
                        if (m != '#' && str.length > oldString.length) {
                            vlrWithMask += m
                            continue
                        }
                        try {
                            vlrWithMask += str.get(i)
                        } catch (e: Exception) {
                            break
                        }
                        i++
                    }

                    isUpdating = true
                    valor.setText(vlrWithMask)
                    valor.setSelection(vlrWithMask.length)

                }

                override fun afterTextChanged(editable: Editable) {

                }
            }

            return textWatcher
        }
    }
}