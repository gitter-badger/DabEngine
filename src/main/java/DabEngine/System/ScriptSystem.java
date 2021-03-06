package DabEngine.System;

import org.python.util.PythonInterpreter;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CScript;
import DabEngine.Graphics.Graphics;


public class ScriptSystem extends ComponentSystem {
	
	private PythonInterpreter PyI = new PythonInterpreter();

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for(Entity e : EntityManager.entitiesWithComponents(CScript.class)) {
			CScript s = e.getComponent(CScript.class);
			if(s.fileName.isEmpty()) {
				PyI.exec(s.scriptSource);
			}
			else if(s.scriptSource.isEmpty()) {
				PyI.execfile(s.fileName);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}
