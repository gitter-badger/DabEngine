package DabEngine.Graphics;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shaders {

    private int program;
    private int vs;
    private int fs;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public Shaders(File filevs, File filefs){
        program = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filevs));
        glCompileShader(vs);
        if(glGetShaderi(vs, GL_COMPILE_STATUS) == 0){
            LOGGER.log(Level.SEVERE, "Failed compiling vertex shader " + vs);
            LOGGER.log(Level.SEVERE, glGetShaderInfoLog(vs, glGetShaderi(vs, GL_INFO_LOG_LENGTH)));
            System.exit(0);
        }
        glAttachShader(program, vs);

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,readFile(filefs));
        glCompileShader(fs);
        if(glGetShaderi(fs, GL_COMPILE_STATUS) == 0){
            LOGGER.log(Level.SEVERE, "Failed compiling fragment shader " + fs);
            LOGGER.log(Level.SEVERE, glGetShaderInfoLog(fs, glGetShaderi(fs, GL_INFO_LOG_LENGTH)));
            System.exit(0);
        }
        glAttachShader(program, fs);

        glLinkProgram(program);
        glValidateProgram(program);

    }
    
    public Shaders(String source_vs, String source_fs) {
    	program = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, source_vs);
        glCompileShader(vs);
        if(glGetShaderi(vs, GL_COMPILE_STATUS) == 0){
        	LOGGER.log(Level.SEVERE, "Failed compiling vertex shader " + vs);
            LOGGER.log(Level.SEVERE, glGetShaderInfoLog(vs, glGetShaderi(vs, GL_INFO_LOG_LENGTH)));
            System.exit(0);
        }
        glAttachShader(program, vs);

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, source_fs);
        glCompileShader(fs);
        if(glGetShaderi(fs, GL_COMPILE_STATUS) == 0){
        	LOGGER.log(Level.SEVERE, "Failed compiling fragment shader " + fs);
            LOGGER.log(Level.SEVERE, glGetShaderInfoLog(fs, glGetShaderi(fs, GL_INFO_LOG_LENGTH)));
            System.exit(0);
        }
        glAttachShader(program, fs);

        glLinkProgram(program);
        glValidateProgram(program);
    }
    
    public Shaders(InputStream stream_vs, InputStream stream_fs) {
    	program = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFileFromStream(stream_vs));
        glCompileShader(vs);
        if(glGetShaderi(vs, GL_COMPILE_STATUS) == 0){
        	LOGGER.log(Level.SEVERE, "Failed compiling vertex shader " + vs);
            LOGGER.log(Level.SEVERE, glGetShaderInfoLog(vs, glGetShaderi(vs, GL_INFO_LOG_LENGTH)));
            System.exit(0);
        }
        glAttachShader(program, vs);

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFileFromStream(stream_fs));
        glCompileShader(fs);
        if(glGetShaderi(fs, GL_COMPILE_STATUS) == 0){
        	LOGGER.log(Level.SEVERE, "Failed compiling fragment shader " + fs);
            LOGGER.log(Level.SEVERE, glGetShaderInfoLog(fs, glGetShaderi(fs, GL_INFO_LOG_LENGTH)));
            System.exit(0);
        }
        glAttachShader(program, fs);

        glLinkProgram(program);
        glValidateProgram(program);
    }

    private String readFile(File file){
        StringBuilder string = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine()) != null){
                string.append(line);
                string.append("\n");
            }
        }catch(IOException e){
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return string.toString();
    }
    
    private String readFileFromStream(InputStream stream) {
    	StringBuilder string = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(stream))){
            String line;
            while((line = br.readLine()) != null){
                string.append(line);
                string.append("\n");
            }
        }catch(IOException e){
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return string.toString();
    }

    public void bind(){
        glUseProgram(program);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public void setUniform(String uniformName, Matrix4f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
            LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);
        value.get(buffer);
        glUniformMatrix4fv(location, false, buffer);
    }

    public void setUniform(String uniformName, int value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        glUniform1i(location, value);
    }
    
    public void setUniform(String uniformName, float value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        glUniform1f(location, value);
    }
    
    public void setUniform(String uniformName, Vector4f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        value.get(buffer);
        glUniform4fv(location, buffer);
    }
    
    public void setUniform(String uniformName, Vector3f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        value.get(buffer);
        glUniform3fv(location, buffer);
    }
    
    public void setUniform(String uniformName, Vector2f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        value.get(buffer);
        glUniform2fv(location, buffer);
    }
    
    private final String errorMessage(String uniformName, int location) {
    	return "Could not set uniform " + uniformName + " because location is " + location;
    }
}
