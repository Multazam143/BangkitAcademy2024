import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.tugasdicoding.R
import com.dicoding.tugasdicoding.data.response.ListEventsItem
import com.dicoding.tugasdicoding.DetailActivity

class DetailAdapter(private val events: List<ListEventsItem>) : RecyclerView.Adapter<DetailAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageCover: ImageView = view.findViewById(R.id.imageCover)
        val eventName: TextView = view.findViewById(R.id.eventName)
        val root: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_detail, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.name
        Glide.with(holder.itemView.context).load(event.imageLogo).into(holder.imageCover)

        holder.root.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = events.size
}
