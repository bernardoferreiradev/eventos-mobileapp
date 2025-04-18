package com.example.eventospt.DepoisDoLogin.Utilizador

import com.example.eventospt.helpers.Item
import com.example.eventospt.helpers.ItemAdapter
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.eventospt.R
import com.google.android.material.navigation.NavigationView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventospt.helpers.clearUserCache
import com.example.eventospt.helpers.loadUserDataFromCache
import com.example.eventospt.helpers.saveUserDataToCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.eventospt.helpers.terminarSessao
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Inicio : AppCompatActivity() {

    private lateinit var inicioLayout: TextView
    private lateinit var comediaLayout: TextView
    private lateinit var fotografiaLayout: TextView
    private lateinit var restauracaoLayout: TextView
    private lateinit var limpezaLayout: TextView
    private lateinit var musicaLayout: TextView
    private lateinit var CriarEvento: Button
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var Perfil: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var ArrastarAtualizar: SwipeRefreshLayout
    private val itemList = mutableListOf<Item>()
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dlu_inicio)

        inicioLayout = findViewById(R.id.option_inicio)
        comediaLayout = findViewById(R.id.option_comedia)
        fotografiaLayout = findViewById(R.id.option_fotografia)
        restauracaoLayout = findViewById(R.id.option_restauracao)
        limpezaLayout = findViewById(R.id.option_limpeza)
        musicaLayout = findViewById(R.id.option_musica)

        CriarEvento = findViewById(R.id.criar)

        inicioLayout.setOnClickListener { AbrirActivityInicio() }
        comediaLayout.setOnClickListener { AbrirActivity("Comédia") }
        fotografiaLayout.setOnClickListener { AbrirActivity("Fotografia") }
        restauracaoLayout.setOnClickListener { AbrirActivity("Restauração") }
        limpezaLayout.setOnClickListener { AbrirActivity("Limpeza") }
        musicaLayout.setOnClickListener { AbrirActivity("Música") }


        ArrastarAtualizar = findViewById(R.id.arrastar_para_atualizar)

        ArrastarAtualizar.setOnRefreshListener {

            clearUserCache(this)


            loadUserData()


            fetchItemsFromFirebase()


            ArrastarAtualizar.isRefreshing = false
        }


        Perfil = findViewById(R.id.third_icon)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        val headerView = navigationView.getHeaderView(0)
        userNameTextView = headerView.findViewById(R.id.nomeutilizador)
        userEmailTextView = headerView.findViewById(R.id.emailutilizador)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Perfil.setOnClickListener{
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        CriarEvento.setOnClickListener{
            val intent = Intent(this, CriarEventoActivity::class.java)
            startActivity(intent)
            finish()
        }

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            findViewById(R.id.toolbar),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, Inicio::class.java)
                    startActivity(intent)
                    finish()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_mapa -> {
                    val intent = Intent(this, GoogleMapsActivity::class.java)
                    startActivity(intent)
                    finish()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_os_meus_eventos -> {
                    val intent = Intent(this, ListaEventosCriadosActivity::class.java)
                    startActivity(intent)
                    finish()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_whentomeet -> {
                    val intent = Intent(this, ListaWhen2MeetCriados::class.java)
                    startActivity(intent)
                    finish()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_profile -> {
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    finish()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_visitas -> {
                    val intent = Intent(this, Visitas::class.java)
                    startActivity(intent)
                    finish()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_favoritos -> {
                    val intent = Intent(this, FavoritosActivity::class.java)
                    startActivity(intent)
                    finish()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_notifications -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_logout -> {
                    terminarSessao(this)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }

        val secondIcon = findViewById<ImageView>(R.id.second_icon)
        secondIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        toggle.syncState()

        //executa funcoes
        loadUserData()
        setupRecyclerView()
        fetchItemsFromFirebase()
    }

    private fun AbrirActivity(opcao: String) {
        val intent = Intent(this, EscolhaCategoriaActivity::class.java)
        intent.putExtra("opcao", opcao)
        startActivity(intent)
        finish()
    }


    private fun AbrirActivityInicio() {
        val intent = Intent(this, Inicio::class.java)
        startActivity(intent)
        finish()
    }


    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = ItemAdapter(itemList) { selectedItem ->
            val intent = Intent(this, DetalhesServicoActivity::class.java).apply {
                putExtra("item_nome", selectedItem.nome)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }


    private fun loadUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("utilizadores").child(userId)

            val cachedData = loadUserDataFromCache(this)
            val cachedName = cachedData["userName"]
            val cachedEmail = cachedData["userEmail"]
            val cachedApelido = cachedData["apelido"]
            val cachedDOB = cachedData["dataNascimento"]

            if (!cachedName.isNullOrEmpty() && !cachedEmail.isNullOrEmpty() && !cachedApelido.isNullOrEmpty() && !cachedDOB.isNullOrEmpty()) {
                userNameTextView.text = "$cachedName $cachedApelido"
                userEmailTextView.text = cachedEmail

            } else {
                userRef.get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val NomeProprio = dataSnapshot.child("nome").getValue(String::class.java) ?: "Nome não disponível"
                        val userEmail = dataSnapshot.child("email").getValue(String::class.java) ?: "Email não disponível"
                        val Apelido = dataSnapshot.child("apelido").getValue(String::class.java) ?: "Apelido não disponível"
                        val DataNascimento = dataSnapshot.child("data_nascimento").getValue(String::class.java) ?: "Data não disponível"

                        userNameTextView.text = "$NomeProprio $Apelido"
                        userEmailTextView.text = userEmail

                        saveUserDataToCache(this, NomeProprio, userEmail, Apelido, DataNascimento)
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao carregar dados: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchItemsFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("fornecedores")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()

                val validItems = mutableListOf<Item>()

                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(Item::class.java)

                    item?.let {
                        if (it.status_candidatura?.equals("Aceite", ignoreCase = true) == true && it.valor_parceria?.equals("Parceria Premium", ignoreCase = true) == true) {
                            validItems.add(it)
                        }
                    }
                }

                if (validItems.isNotEmpty()) {
                    validItems.shuffle()
                    itemList.addAll(validItems.take(5))
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Inicio, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
