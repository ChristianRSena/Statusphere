package com.example.statusphere_project

import android.os.Bundle
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.statusphere_project.LoginActivity.DatabaseHelper
import android.widget.Button
import java.sql.SQLException
import android.widget.Toast
import android.widget.EditText
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import android.opengl.GLU
import javax.microedition.khronos.opengles.GL10
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import android.opengl.GLSurfaceView
import android.opengl.EGL14
import android.opengl.GLSurfaceView.RENDERMODE_CONTINUOUSLY
import android.view.MotionEvent
import javax.microedition.khronos.egl.EGLConfig





class HomeActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var addButton: Button
    private lateinit var editTextEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        MobileAds.initialize(this, OnInitializationCompleteListener {

        })

        val dao = DatabaseDAO()
        addButton = findViewById(R.id.btnAddFriend)
        var isFriendFound = false


        val userId =
            intent.getIntExtra("userId", -1) // -1 is a default value if userId is not found
        databaseHelper = DatabaseHelper()
        addButton.setOnClickListener {
            val email: String? = intent.getStringExtra("userId")
            if (email != null) {
                // Perform a search in the database for the provided email address
                val isFriendFound = databaseHelper.searchFriendByEmail(email)
            } else {
                // Handle the case where userId extra is not found
            }



            if (isFriendFound) {
                // Friend found in the database, perform appropriate action (e.g., add friend to list)
                Toast.makeText(this, "Friend found and added!", Toast.LENGTH_SHORT).show()
            } else {
                // Friend not found in the database
                Toast.makeText(this, "Friend not found!", Toast.LENGTH_SHORT).show()
            }
            if (userId != -1) {
                // Use userId as needed in your HomeActivity

            } else {
                // Handle the case where userId is not found
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            val friends = dao.getFriends(userId)
            val groups = dao.getGroups(userId)


            // Add code to initialize and display user's friends, groups, and sphere visualization
        }
    }


    class DatabaseDAO {

        // JDBC connection parameters
        private val url =
            "jdbc:mysql://statusphere-server.database.windows.net/statusphere-server/Statusphere"
        private val username = "sena"
        private val password = "temp"

        // Method to fetch user's friends
        fun getFriends(userId: Int): List<String> {
            val friends = mutableListOf<String>()

            // Establish database connection
            DriverManager.getConnection(url, username, password).use { connection ->
                // Prepare SQL query
                val sql = "SELECT name FROM friends WHERE user_id = ?"
                val statement = connection.prepareStatement(sql)

                // Set parameters
                statement.setInt(1, userId)

                // Execute query
                val resultSet = statement.executeQuery()

                // Process result set
                while (resultSet.next()) {
                    val friendName = resultSet.getString("name")
                    friends.add(friendName)
                }
            }

            return friends
        }

        // Method to fetch user's groups
        fun getGroups(userId: Int): List<String> {
            val groups = mutableListOf<String>()

            // Establish database connection
            DriverManager.getConnection(url, username, password).use { connection ->
                // Prepare SQL query
                val sql = "SELECT name FROM groups WHERE user_id = ?"
                val statement = connection.prepareStatement(sql)

                // Set parameters
                statement.setInt(1, userId)

                // Execute query
                val resultSet = statement.executeQuery()

                // Process result set
                while (resultSet.next()) {
                    val groupName = resultSet.getString("name")
                    groups.add(groupName)
                }
            }

            return groups
        }
    }

    class SphereView(context: Context, attrs: AttributeSet?) : GLSurfaceView(context, attrs), View.OnTouchListener {
        private val renderer: SphereRenderer
        private lateinit var sphere: Sphere
        private lateinit var light: Light

        private var previousX: Float = 0f
        private var previousY: Float = 0f
        private var rotationX: Float = 0f
        private var rotationY: Float = 0f

        init {
            setEGLContextClientVersion(2)
            renderer = SphereRenderer()
            setRenderer(renderer)
            renderMode = RENDERMODE_CONTINUOUSLY
            setOnTouchListener(this)
        }

        private inner class SphereRenderer : GLSurfaceView.Renderer {
            override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
                gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
                gl.glEnable(GL10.GL_DEPTH_TEST)
                gl.glEnable(GL10.GL_LIGHTING)

                sphere = Sphere(1.0f, 36, 36)
                light = Light(1.0f, 1.0f, 1.0f, 0.0f)
            }

            override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
                gl.glViewport(0, 0, width, height)
                gl.glMatrixMode(GL10.GL_PROJECTION)
                gl.glLoadIdentity()
                val ratio = width.toFloat() / height.toFloat()
                GLU.gluPerspective(gl, 45.0f, ratio, 0.1f, 100.0f)
                gl.glMatrixMode(GL10.GL_MODELVIEW)
                gl.glLoadIdentity()
                GLU.gluLookAt(gl, 0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
            }

            override fun onDrawFrame(gl: GL10) {
                gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
                gl.glMatrixMode(GL10.GL_MODELVIEW)
                gl.glLoadIdentity()

                gl.glTranslatef(0.0f, 0.0f, -5.0f)
                gl.glRotatef(rotationX, 1.0f, 0.0f, 0.0f)
                gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f)

                light.enable(gl)
                sphere.draw(gl)
            }
        }

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    previousX = event.x
                    previousY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - previousX
                    val dy = event.y - previousY

                    rotationX += dy / 2
                    rotationY += dx / 2

                    previousX = event.x
                    previousY = event.y
                }
            }
            return true
        }
    }


    class Sphere(
        private val radius: Float,
        private val slices: Int,
        private val stacks: Int
    ) {
        private val vertices: FloatArray
        private val normals: FloatArray
        private val texCoords: FloatArray
        private val indices: ShortArray

        init {
            val vertexData = mutableListOf<Float>()
            val normalData = mutableListOf<Float>()
            val texCoordData = mutableListOf<Float>()
            val indexData = mutableListOf<Short>()

            for (stackCount in 0 until stacks) {
                val stackRad = Math.PI / 2 - stackCount * Math.PI / stacks
                val texV = 1 - stackCount.toFloat() / stacks

                for (sliceCount in 0 until slices) {
                    val sliceRad = 2 * Math.PI * sliceCount / slices
                    val texU = sliceCount.toFloat() / slices
                    val x = (radius * Math.cos(stackRad) * Math.cos(sliceRad)).toFloat()
                    val y = (radius * Math.sin(stackRad)).toFloat()
                    val z = (radius * Math.cos(stackRad) * Math.sin(sliceRad)).toFloat()
                    val nx = x / radius
                    val ny = y / radius
                    val nz = z / radius

                    vertexData.add(x)
                    vertexData.add(y)
                    vertexData.add(z)
                    normalData.add(nx)
                    normalData.add(ny)
                    normalData.add(nz)
                    texCoordData.add(texU)
                    texCoordData.add(texV)
                }
            }

            for (stackCount in 0 until stacks - 1) {
                val stackStart = stackCount * slices
                for (sliceCount in 0 until slices) {
                    val nextSlice = (sliceCount + 1) % slices
                    indexData.add((stackStart + sliceCount).toShort())
                    indexData.add(((stackStart + slices) + sliceCount).toShort())
                    indexData.add(((stackStart + slices) + nextSlice).toShort())
                    indexData.add((stackStart + sliceCount).toShort())
                    indexData.add(((stackStart + slices) + nextSlice).toShort())
                    indexData.add((stackStart + nextSlice).toShort())
                }
            }

            vertices = vertexData.toFloatArray()
            normals = normalData.toFloatArray()
            texCoords = texCoordData.toFloatArray()
            indices = indexData.toShortArray()
        }

        private fun FloatArray.toFloatBuffer(): FloatBuffer {
            val buffer = ByteBuffer.allocateDirect(size * Float.SIZE_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
            buffer.put(this)
            buffer.position(0)
            return buffer
        }

        private fun ShortArray.toShortBuffer(): ShortBuffer {
            val buffer = ByteBuffer.allocateDirect(size * Short.SIZE_BYTES)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
            buffer.put(this)
            buffer.position(0)
            return buffer
        }

        fun draw(gl: GL10) {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY)
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

            val vertexBuffer = vertices.toFloatBuffer()
            val normalBuffer = normals.toFloatBuffer()
            val texCoordBuffer = texCoords.toFloatBuffer()

            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
            gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer)
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer)

            val indexBuffer = indices.toShortBuffer()
            gl.glDrawElements(GL10.GL_TRIANGLES, indices.size, GL10.GL_UNSIGNED_SHORT, indexBuffer)

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY)
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
        }
    }

    class Light(
        private val ambientR: Float,
        private val ambientG: Float,
        private val ambientB: Float,
        private val specular: Float
    ) {
        fun enable(gl: GL10) {
            val materialAmbient = floatArrayOf(ambientR, ambientG, ambientB, 1.0f)
            val materialSpecular = floatArrayOf(specular, specular, specular, 1.0f)
            val lightPosition = floatArrayOf(0.0f, 0.0f, 2.0f, 1.0f)

            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, materialAmbient, 0)
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, materialSpecular, 0)
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0)

            gl.glEnable(GL10.GL_LIGHT0)
        }
    }

}

