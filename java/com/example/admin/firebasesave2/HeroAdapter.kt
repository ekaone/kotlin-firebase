package com.example.admin.firebasesave2

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class HeroAdapter(val mCtx: Context, val layoutResId: Int, val heroList: List<Hero>)
    : ArrayAdapter<Hero>(mCtx, layoutResId, heroList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewName = view.findViewById<TextView>(R.id.textViewName)
        val textViewUpdate = view.findViewById<TextView>(R.id.textViewUpdate)

        val hero = heroList[position]
        textViewName.text = hero.name

        textViewUpdate.setOnClickListener {
            showUpdateDialog(hero)
        }

        return view
    }

    fun showUpdateDialog(hero: Hero) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Update Hero")

        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.layout_update_hero, null)

        val editText = view.findViewById<EditText>(R.id.editTextName)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        editText.setText(hero.name)
        ratingBar.rating = hero.rating.toFloat()

        builder.setView(view)
        builder.setPositiveButton("Update") {p0, p1 ->
            val dbHero = FirebaseDatabase.getInstance().getReference("heroes")
            val name = editText.text.toString().trim()

            if(name.isEmpty()) {
                editText.error = "Please enter a name"
                editText.requestFocus()
                return@setPositiveButton
            }

            val hero = Hero(hero.id, name, ratingBar.rating.toInt())
            dbHero.child(hero.id).setValue(hero)

            Toast.makeText(mCtx, "Hero Updated", Toast.LENGTH_LONG).show()

        }
        builder.setNegativeButton("No") {p0, p1 ->

        }

        val alert = builder.create()
        alert.show()
    }

}