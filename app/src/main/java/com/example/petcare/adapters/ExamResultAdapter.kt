
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.databinding.ResultListrResultBinding

class ExamResultAdapter(
    private val ExamResultList: List<String>?,
    private val context: Context,
    private val onResultClicked: (String) -> Unit
) :
    RecyclerView.Adapter<ExamResultAdapter.ExamResultViewHolder>() {


    class ExamResultViewHolder(private var binding: ResultListrResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: String) {
            Glide.with(binding.img).load(result).into(binding.img)
        }
    }

    override fun getItemCount(): Int {
        return this.ExamResultList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamResultViewHolder {
        return ExamResultViewHolder(
            ResultListrResultBinding.inflate(
                LayoutInflater.from(
                    parent
                        .context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ExamResultViewHolder, position: Int) {
        val current = ExamResultList!![position]
        holder.itemView.setOnClickListener { onResultClicked(current) }
        /*holder.itemView.setOnClickListener {
            Toast.makeText(
                context,
                ExamResultList!![position],
                Toast.LENGTH_SHORT
            ).show()
        }*/
        holder.bind(current)
    }

}