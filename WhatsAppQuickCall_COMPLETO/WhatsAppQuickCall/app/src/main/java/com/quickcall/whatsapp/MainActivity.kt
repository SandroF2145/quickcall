package com.quickcall.whatsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Lista de contatos com números
    private val contacts = mapOf(
        "Sandro Filho" to "5534997802677",
        "Sarah" to "5534998944208",
        "Solange" to "5534997915530",
        "Karla" to "553497936640",
        "Rita" to "553496338437",
        "Iraides" to "553498071226",
        "Rosângela" to "553496605330"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar ListView com adapter customizado
        val listView = findViewById<ListView>(R.id.contactsList)
        val contactNames = contacts.keys.toList()
        val adapter = ContactAdapter(this, contactNames)
        listView.adapter = adapter

        // Configurar clique na lista
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedName = contactNames[position]
            val phoneNumber = contacts[selectedName]
            
            if (phoneNumber != null) {
                openWhatsAppAndCall(selectedName, phoneNumber)
            }
        }
    }

    private fun openWhatsAppAndCall(name: String, number: String) {
        try {
            // Mostrar mensagem
            Toast.makeText(this, "Ligando para $name...", Toast.LENGTH_SHORT).show()

            // Abrir WhatsApp direto na tela de chamada
            try {
                // Tenta abrir direto a chamada de voz
                val callIntent = Intent(Intent.ACTION_VIEW)
                callIntent.data = Uri.parse("https://api.whatsapp.com/send?phone=$number")
                callIntent.setPackage("com.whatsapp")
                startActivity(callIntent)
            } catch (e: Exception) {
                // Se falhar, abre apenas o chat
                val chatIntent = Intent(Intent.ACTION_VIEW)
                chatIntent.data = Uri.parse("https://wa.me/$number")
                chatIntent.setPackage("com.whatsapp")
                startActivity(chatIntent)
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Erro: WhatsApp não instalado", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Quando voltar para o app, ele já estará na tela inicial
        Toast.makeText(this, "Pronto para nova chamada", Toast.LENGTH_SHORT).show()
    }
}

// Adapter customizado para exibir os contatos com layout melhorado
class ContactAdapter(context: Context, private val contacts: List<String>) : 
    ArrayAdapter<String>(context, R.layout.contact_item, contacts) {
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.contact_item, parent, false)
        
        val contactName = view.findViewById<TextView>(R.id.contactName)
        contactName.text = contacts[position]
        
        return view
    }
}
