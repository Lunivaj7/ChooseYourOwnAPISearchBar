package com.example.chooseyourownapi

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchPokemonImage()
        val button = findViewById<Button>(R.id.button)
        setupButton(button)
    }


    private fun fetchPokemonImage() {
        val randomId = (1..1025).random()
        val client = AsyncHttpClient()

        client["https://pokeapi.co/api/v2/pokemon/$randomId", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Pokemon", "response successful: $json")
                val imageView = findViewById<ImageView>(R.id.pokemonImage)
                val nameText = findViewById<TextView>(R.id.pokemonName)
                val typeText = findViewById<TextView>(R.id.pokemonType)

                val jsonObject = json.jsonObject
                val name = json.jsonObject.getString("name")
                val imageUrl = json.jsonObject.getJSONObject("sprites").getString("front_default")

                val typesArray = jsonObject.getJSONArray("types")
                val type = typesArray.getJSONObject(0).getJSONObject("type").getString("name")


                Log.d("Pokemon", "Name: $name")
                Log.d("Pokemon", "Image URL: $imageUrl")
                Log.d("Pokemon", "Type: $type")

                Glide.with(this@MainActivity)
                    .load(imageUrl)
                    .fitCenter()
                    .into(imageView)

                nameText.text = "Name: ${name.replaceFirstChar { it.uppercase() }}"
                typeText.text = "Type: ${type.replaceFirstChar { it.uppercase() }}"
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Pokemon Error", errorResponse)
            }
        }]
    }

    private fun setupButton(button: Button) {
        button.setOnClickListener {
            fetchPokemonImage()
        }
    }

}