package View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medication_reminders_app.R
import com.example.medication_reminders_app.data.Journal

class JournalAdapter(val context: Context,
                     val journalDeleteInterface: JournalDeleteInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val allJournals = ArrayList<Journal>()

    override fun getItemViewType(position: Int): Int {
        if (allJournals[position].isTaken) {
            return R.layout.journal_rv_item_green
        } else {
            return R.layout.journal_rv_item_red
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        if (viewType == R.layout.journal_rv_item_green) {
            return GreenViewHolder(view, journalDeleteInterface)
        }

        return RedViewHolder(view, journalDeleteInterface)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GreenViewHolder) {
            holder.bind(allJournals[position])
        } else if (holder is RedViewHolder) {
            holder.bind(allJournals[position])
        }
    }

    override fun getItemCount(): Int {
        return allJournals.size
    }

    class GreenViewHolder(view: View, val journalDeleteInterface: JournalDeleteInterface) : RecyclerView.ViewHolder(view) {
        private var cureTitle: TextView = view.findViewById(R.id.textViewName)
        private var cureDosage: TextView = view.findViewById(R.id.textViewDosage)
        private var cureDate: TextView = view.findViewById(R.id.textViewDate)
        private var buttonDelete: TextView = view.findViewById(R.id.buttonDelete)

        fun bind(journal: Journal) {
            cureTitle.setText(journal.cureTitle)
            cureDosage.setText(journal.dosage.toString() + " мг")
            cureDate.setText(journal.date)

            buttonDelete.setOnClickListener {
                journalDeleteInterface.onDeleteClick(journal)
            }
        }
    }

    class RedViewHolder(view: View, val journalDeleteInterface: JournalDeleteInterface) : RecyclerView.ViewHolder(view) {
        private var cureTitle: TextView = view.findViewById(R.id.textViewName)
        private var cureDosage: TextView = view.findViewById(R.id.textViewDosage)
        private var cureDate: TextView = view.findViewById(R.id.textViewDate)
        private var buttonDelete: TextView = view.findViewById(R.id.buttonDelete)

        fun bind(journal: Journal) {
            cureTitle.setText(journal.cureTitle)
            cureDosage.setText(journal.dosage.toString() + " мг")
            cureDate.setText(journal.date)

            buttonDelete.setOnClickListener {
                journalDeleteInterface.onDeleteClick(journal)
            }
        }
    }

    fun updateList(newList: List<Journal>) {
        allJournals.clear()
        allJournals.addAll(newList)
        notifyDataSetChanged()
    }
}

interface JournalDeleteInterface {
    fun onDeleteClick(journal: Journal)
}