package com.example.imc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Relie le code Kotlin au fichier XML activity_main.xml
        setContentView(R.layout.activity_main)

        // Récupération des éléments de l'interface par leur ID
        val etPoids = findViewById<EditText>(R.id.etPoids)
        val etTaille = findViewById<EditText>(R.id.etTaille)
        val btnCalculer = findViewById<Button>(R.id.btnCalculer)
        val btnEffacer = findViewById<Button>(R.id.btnEffacer)
        val tvResultatImc = findViewById<TextView>(R.id.tvResultatImc)
        val tvResultatCategorie = findViewById<TextView>(R.id.tvResultatCategorie)

        // Action du bouton Calculer
        btnCalculer.setOnClickListener {
            val poidsStr = etPoids.text.toString()
            val tailleStr = etTaille.text.toString()

            // 1. Vérification si les champs sont vides
            if (poidsStr.isEmpty()) {
                etPoids.error = getString(R.string.erreur_vide)
                return@setOnClickListener
            }
            if (tailleStr.isEmpty()) {
                etTaille.error = getString(R.string.erreur_vide)
                return@setOnClickListener
            }

            val poids = poidsStr.toFloatOrNull()
            val taille = tailleStr.toFloatOrNull()

            // 2. Vérification si les valeurs sont valides (> 0)
            if (poids == null || poids <= 0) {
                etPoids.error = getString(R.string.erreur_valeur)
                return@setOnClickListener
            }
            if (taille == null || taille <= 0) {
                etTaille.error = getString(R.string.erreur_valeur)
                return@setOnClickListener
            }

            // 3. Calcul de l'IMC
            val imc = poids / (taille * taille)

            // 4. Affichage du résultat formaté via strings.xml
            tvResultatImc.text = getString(R.string.resultat_imc_val, imc)

            // 5. Détermination de la catégorie et de la couleur
            val (categorie, couleurRes) = when {
                imc < 18.5 -> "Insuffisance pondérale" to R.color.imc_alerte
                imc < 25 -> "Corpulence normale" to R.color.imc_normal
                imc < 30 -> "Surpoids" to R.color.imc_alerte
                imc < 35 -> "Obésité modérée" to R.color.imc_obese
                imc < 40 -> "Obésité sévère" to R.color.imc_obese
                else -> "Obésité morbide" to R.color.imc_morbide
            }

            tvResultatCategorie.text = getString(R.string.resultat_categorie_val, categorie)
            tvResultatCategorie.setTextColor(ContextCompat.getColor(this, couleurRes))
        }

        // Action du bouton Effacer
        btnEffacer.setOnClickListener {
            etPoids.text.clear()
            etTaille.text.clear()
            tvResultatImc.text = getString(R.string.init_imc)
            tvResultatCategorie.text = getString(R.string.init_cat)
            tvResultatCategorie.setTextColor(ContextCompat.getColor(this, R.color.black))
            
            // Remettre le focus sur le champ poids
            etPoids.requestFocus()
            
            Toast.makeText(this, "Champs réinitialisés", Toast.LENGTH_SHORT).show()
        }
    }
}
