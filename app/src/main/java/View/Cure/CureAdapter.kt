package View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medication_reminders_app.R
import com.example.medication_reminders_app.data.Cure.Cure

class CureAdapter(val context: Context,
                  val cureDeleteInterface: CureDeleteInterface,
                  val cureEditInterface: CureEditInterface,
                  val cureClickInterface: CureClickInterface) :
    RecyclerView.Adapter<CureAdapter.ViewHolder>() {

    private val allCures = ArrayList<Cure>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cureName = itemView.findViewById<TextView>(R.id.textViewName)
        val cureDosage = itemView.findViewById<TextView>(R.id.textViewDosage)
        val buttonDelete = itemView.findViewById<Button>(R.id.buttonDelete)
        val buttonEdit = itemView.findViewById<Button>(R.id.buttonEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.cure_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cureName.setText(allCures.get(position).title)
        holder.cureDosage.setText("Дозировка: " + allCures.get(position).dosage + " мг")

        holder.buttonDelete.setOnClickListener {
            cureDeleteInterface.onDeleteClick(allCures.get(position))
        }

        holder.itemView.setOnClickListener {
            cureClickInterface.onCureClick(allCures.get(position))
        }

        holder.buttonEdit.setOnClickListener {
            cureEditInterface.onEditClick(allCures.get(position))
        }
    }

    override fun getItemCount(): Int {
        return allCures.size
    }

    fun updateList(newList: List<Cure>) {
        allCures.clear()
        allCures.addAll(newList)
        notifyDataSetChanged()
    }
}

interface CureDeleteInterface {
    fun onDeleteClick(cure: Cure)
}

interface CureClickInterface {
    fun onCureClick(cure: Cure)
}

interface CureEditInterface {
    fun onEditClick(cure: Cure)
}