package david.itemtracker

import android.arch.persistence.room.Room
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class characterAdapter(characters : List<String>) : RecyclerView.Adapter<characterAdapter.ViewHolder>(){
    private var characterList: List<String> = characters

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.character_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(characterList[position])
    }

    override fun getItemCount() = characterList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(characters:String) {
            val textViewName = itemView.findViewById(R.id.textCharacterName) as TextView
            textViewName.text = characters
        }
    }
}
