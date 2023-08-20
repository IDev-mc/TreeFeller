package de.I_Dev.TF.API;

import java.util.List;

import de.I_Dev.TF.TreeFeller;

public class TreeFellerAPI {

	public List<String> getTreeMaterials() {
		return TreeFeller.treematerials;
	}
	
	public int getMaxTreeSize() {
		return TreeFeller.maxTreeSize;
	}
	
	public int getMaxTreeHight() {
		return TreeFeller.maxTreeHeight;
	}
	
	public boolean isToolDamageEnabled() {
		return TreeFeller.toolDamage;
	}
	
	public boolean isOnSneakEnabled() {
		return TreeFeller.onsneak;
	}
	
	
	public void setTreeMaterials(List<String> materials) {
		TreeFeller.treematerials = materials;
	}
	
	public void setMaxTreeSize(int maxTreeSize) {
		TreeFeller.maxTreeSize = maxTreeSize;
	}
	
	public void setMaxTreeHight(int maxTreeHight) {
		TreeFeller.maxTreeHeight = maxTreeHight;
	}
	
	public void setToolDamage(boolean toolDamage) {
		TreeFeller.toolDamage = toolDamage;
	}
	
	public void setOnSneak(boolean onSneak) {
		TreeFeller.onsneak = onSneak;
	}
}
