package com.hcl.elch.freshersuperchargers.trainingworkflow.controller;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcl.elch.freshersuperchargers.trainingworkflow.entity.Task;
import com.hcl.elch.freshersuperchargers.trainingworkflow.repo.TaskRepo;

public class GithubUrlValidation implements JavaDelegate {

	@Autowired
	private TaskRepo tr;

	public long id;

	final Logger log = LogManager.getLogger(GithubUrlValidation.class.getName());

	/*
	 * public static void main(String args[]) throws InvalidRemoteException,
	 * TransportException, GitAPIException {
	 * 
	 * Collection<Ref> refs; List<String> branches = new ArrayList<String>(); try {
	 * refs = Git.lsRemoteRepository() .setHeads(true)
	 * .setRemote("https://github.com/ahmadtausif38/TrainingWorkFlow.git") .call();
	 * for (Ref ref : refs) {
	 * branches.add(ref.getName().substring(ref.getName().lastIndexOf("/")+1,
	 * ref.getName().length())); } Collections.sort(branches); } catch
	 * (InvalidRemoteException e) {
	 * //LOGGER.error(" InvalidRemoteException occured in fetchGitBranches",e);
	 * System.out.printf(" InvalidRemoteException occured in fetchGitBranches",e);
	 * e.printStackTrace(); } catch (TransportException e) {
	 * //LOGGER.error(" TransportException occurred in fetchGitBranches",e);
	 * System.out.printf("  TransportException occurred in fetchGitBranches",e); }
	 * catch (GitAPIException e) {
	 * //LOGGER.error(" GitAPIException occurred in fetchGitBranches",e);
	 * System.out.printf(" GitAPIException occurred in fetchGitBranches",e); }
	 * 
	 * System.out.println("List of Branches : "+branches);
	 * 
	 * 
	 * 
	 * }
	 */
	public void fetchGitBranches(List<String> branches,String url) {
		try {
			Collection<Ref> refs;
			refs = Git.lsRemoteRepository().setHeads(true).setRemote(url).call();
			for (Ref ref : refs) {
				branches.add(ref.getName().substring(ref.getName().lastIndexOf("/") + 1, ref.getName().length()));
			}
			Collections.sort(branches);
		} catch (InvalidRemoteException e) {
			log.error(" InvalidRemoteException occured in fetchGitBranches");
		} catch (TransportException e) {
			log.error("  TransportException occurred in fetchGitBranches");
		} catch (GitAPIException e) {
			log.error(" GitAPIException occurred in fetchGitBranches");
		}
	}
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		try {
			id = TaskController.id;
			log.info("In GITHUB URL VALIDATION TASK");
			String branch = (String) execution.getVariable("BranchName");
			String url = (String) execution.getVariable("githuburl");

			List<String> branches = new ArrayList<String>();
			
			fetchGitBranches(branches,url);

			log.debug("List of Branches : {}", branches);

			if (branches.contains(branch)) {
				execution.setVariable("Decision", "Yes");
				log.info("Decision is Yes");
			} 
			else {
				execution.setVariable("Decision", "No");
				log.info("Decision is No");
			}

		} catch (Exception e) {
			Task t1 = tr.getById(id);
			t1.setStatus("Error");
			tr.save(t1);
		}

	}

}
