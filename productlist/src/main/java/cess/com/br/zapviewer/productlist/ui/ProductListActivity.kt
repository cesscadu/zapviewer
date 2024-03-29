package cess.com.br.zapviewer.productlist.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cess.com.br.zapviewer.common.action.PORTAL_NAME_PARAM
import cess.com.br.zapviewer.common.action.PRODUCT_DETAILS_PARAM
import cess.com.br.zapviewer.common.action.PRODUCT_DETAIL_ACTIVITY
import cess.com.br.zapviewer.common.model.Product
import cess.com.br.zapviewer.productlist.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListActivity : AppCompatActivity(), ProductListAdapter.OnClickProductItem {

    private val pageSize = 20
    private val viewModel: ProductListViewModel by viewModel()

    private val resourceProvider: ResourceProvider by lazy {
        ResourceProvider(this@ProductListActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        initViewModel()

        setSupportActionBar(resourceProvider.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initViewModel() {
        with(viewModel) {
            getProductList(getPortalName().orEmpty())

            loading.observe(
                this@ProductListActivity
            ) {
                if (it.not()) resourceProvider.progressIndicator?.visibility =
                    View.GONE else View.VISIBLE
            }

            result.observe(
                this@ProductListActivity
            ) {
                setupRecyclerView(it)
            }
        }
    }

    private fun setupRecyclerView(result: List<Product>) {
        resourceProvider.productList?.apply {
            val linearLayoutManager = LinearLayoutManager(this@ProductListActivity)
            val products = ArrayList(result.subList(0, 20))

            layoutManager = linearLayoutManager
            adapter = ProductListAdapter(products, this@ProductListActivity)

            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(this@apply, newState)
                    val lastVisibleItemPosition: Int =
                        linearLayoutManager.findLastVisibleItemPosition()
                    val totalItemCount = recyclerView.layoutManager?.itemCount
                    if (totalItemCount == lastVisibleItemPosition + 1) {

                        val fromIndex = products.lastIndex + 1
                        val maxIndex = products.lastIndex + pageSize
                        val toIndex =
                            if (maxIndex > result.lastIndex) result.lastIndex else maxIndex

                        products.addAll(result.subList(fromIndex, toIndex))
                        adapter?.notifyDataSetChanged()
                    }
                }
            }

            addOnScrollListener(scrollListener)
        }
    }

    private fun getPortalName() = intent.getStringExtra(PORTAL_NAME_PARAM)

    override fun onClickProductItem(product: Product) {
        startActivity(Intent(PRODUCT_DETAIL_ACTIVITY).putExtra(PRODUCT_DETAILS_PARAM, product))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}