package david.itemtracker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class characterAdapter(items : List<TrackerEntity>, val clickListener:(TrackerEntity)->Unit) : RecyclerView.Adapter<characterAdapter.ViewHolder>(){

    private var itemList: List<TrackerEntity> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.character_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(itemList[position],clickListener)
    }

    override fun getItemCount() = itemList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(pair: TrackerEntity, clickListener: (TrackerEntity) -> Unit) {
            val textViewName = itemView.findViewById(R.id.textCharacterName) as TextView
            val textViewType = itemView.findViewById(R.id.textCharacterType) as TextView
            textViewType.text = pair.itemType
            textViewName.text = pair.itemName
            itemView.setOnClickListener{clickListener(pair)}
        }
    }
}
