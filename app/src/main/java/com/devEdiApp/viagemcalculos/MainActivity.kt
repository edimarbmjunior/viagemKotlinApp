/* - JAVA
package com.devEdiApp.viagemcalculos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

*/
// - KOTLIN
package com.devEdiApp.viagemcalculos

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.widget.EditText as EditText1


class MainActivity : AppCompatActivity(), View.OnClickListener {

    var vlr: Float = 0.0f
    val localeBR = Locale("pt", "BR")

    override fun onClick(view: View) {

        val id = view.id

        if (id == R.id.buttonCalculate) {
            println("Button Calculate")
            handleCalculate()
        } else {
            //TODO para retirar a validação de gramatica seguir o caminho abaixo
            //TODO Settings -> Editor -> Spelling -> Clicar em "Configure "Spelling" inspenction"
            println("Foi feito um outro evento de click")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // findViewById<>(R.id.editPrice) //Mapeamento no java

        /* Não muito indicado para caso tenha mais de um button na activity
        buttonCalculate.setOnClickListener {
             View.OnClickListener {
                 println("click using listener from Android!")
             }
         }*/

        this.editPrice.addTextChangedListener(Mask.maskMonetary(this.editPrice, localeBR))

        this.buttonCalculate.setOnClickListener(this)
        this.editDistancia.setOnClickListener(this)
    }

    /* Chamada implementada em duas vias no elemento que terá o click e criação da função que será mapeada no elemento
    fun teste(view: View){

    }*/

    private fun handleCalculate() {
        if (isValid()) {

            try {
                //Formula (Distancia * preco) / autonomia
                val distancia = editDistancia.text.toString().toFloat()
                //val price = editPrice.text.toString().toFloat()
                val autonomy = editAutonomy.text.toString().toFloat()

                val resultTotal = ((distancia * vlr) / autonomy)

                /*textResult.setText("Total: R$ ${Mask.montaValor(resultTotal.toString().toDouble(), localeBR, 999999999)}")*/
                textResult.setText(Mask.montaValor(resultTotal.toString().toDouble(), localeBR, 999999999))

            } catch (nfe: NumberFormatException) {
                Toast.makeText(applicationContext/*Ou pode ser colocado "this"*/, getString(R.string.valoresInvalidos), Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(applicationContext/*Ou pode ser colocado "this"*/, getString(R.string.parametrosInavalidos), Toast.LENGTH_LONG).show()
        }
    }

    private fun isValid(): Boolean {
        editDistancia.setText(Mask.replaceEspecialMonetario(editDistancia.text.toString()))
        editAutonomy.setText(Mask.replaceEspecialMonetario(editAutonomy.text.toString()))
        var vlrPrice = acertarValorPrice(Mask.replaceEspecialMonetario(editPrice.text.toString()))
        vlr = vlrPrice
        var validacaoDados = true
        if (editDistancia.text.isNullOrEmpty()
                || editAutonomy.text.isNullOrEmpty()
                || editPrice.text.toString().isNullOrEmpty()) {
            validacaoDados = false
        }

        //Para usar Regex tive que impementar a biblioteca kotlin-stdlib no build.gradle do aplicativo
        /*if (!editDistancia.text.matches(Regex("[0-9]*"))
                || !editPrice.text.matches(Regex("[0-9]*"))
                || !editAutonomy.text.matches(Regex("[0-9]*"))) {
            validacaoDados = false
        }*/

        if (!validacaoDados) {
            if (editDistancia.length() < 1
                    || editAutonomy.length() < 1
                    || editPrice.length() < 1) {
                validacaoDados = false
            }
        }

        return validacaoDados
    }

    private fun acertarValorPrice(vlr: String) : Float{
        val vlrFloat: Float = vlr.toFloat()/100
        return vlrFloat
    }
}
