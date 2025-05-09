package com.example.scenespotnersion2.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scenespotnersion2.R
import com.example.scenespotnersion2.databinding.MoviesItemBinding
import com.example.scenespotnersion2.main.favourites.FavouritesViewModel
import com.example.scenespotnersion2.remote.data.SeriesDBItem
import com.google.firebase.database.FirebaseDatabase

class SeriesAdapter(
    private var seriesList: List<SeriesDBItem?>,
    private val favouritesViewModel: FavouritesViewModel
) :
    RecyclerView.Adapter<SeriesAdapter.ItemViewHolder>() {


    inner class ItemViewHolder(val binding: MoviesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = MoviesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val series = seriesList[position]
        holder.apply {
            binding.apply {
                "IMDB ${series?.rating?.average}".also { binding.tvMovieItemRating.text = it }
                Glide.with(holder.itemView.context)
                    .load(series?.image?.medium)
                    .placeholder(R.drawable.series_placeholder)
                    .centerCrop()
                    .into(binding.ivMovieItemImage)

                binding.showItem.setOnClickListener {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(series!!)
                    itemView.findNavController().navigate(action)
                }

                series?.id?.let { showId->
                    favouritesViewModel.checkFavouriteState(showId.toString()) { isFavourite ->
                        val color =
                            if (isFavourite) ContextCompat.getColor(itemView.context, R.color.orange)
                            else ContextCompat.getColor(itemView.context, R.color.darkBlue)
                        binding.ivFavouriteToggle.setColorFilter(color)
                    }
                    ivFavouriteToggle.setOnClickListener {
                        favouritesViewModel.toggleFavourite(series) { isFavourite ->
                            val color = if (isFavourite) ContextCompat.getColor(itemView.context, R.color.orange)
                            else ContextCompat.getColor(itemView.context, R.color.darkBlue)
                            binding.ivFavouriteToggle.setColorFilter(color)
                        }
                    }
                }
            }
        }
    }

    fun setSeries(newMovieList: List<SeriesDBItem?>) {
        seriesList = newMovieList
        notifyDataSetChanged()
    }
}