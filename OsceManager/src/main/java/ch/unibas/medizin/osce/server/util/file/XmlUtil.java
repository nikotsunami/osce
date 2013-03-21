package ch.unibas.medizin.osce.server.util.file;

import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.unibas.medizin.osce.domain.CheckList;
import ch.unibas.medizin.osce.domain.ChecklistCriteria;
import ch.unibas.medizin.osce.domain.ChecklistOption;
import ch.unibas.medizin.osce.domain.ChecklistQuestion;
import ch.unibas.medizin.osce.domain.ChecklistTopic;
import ch.unibas.medizin.osce.domain.StandardizedRole;

public class XmlUtil {
	
	private static Logger log = Logger.getLogger(XmlUtil.class);
	
	public XmlUtil(){
		
	}
	
	public String writeXml(Long standardiedRoleId, OutputStream os)
	{
		try
		{	
			StandardizedRole standardizedRole = StandardizedRole.findStandardizedRole(standardiedRoleId);
			
			CheckList checkList = standardizedRole.getCheckList();
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			Document doc = docBuilder.newDocument();
			
			Element pListElement = doc.createElement("plist");
			doc.appendChild(pListElement);
			
			pListElement.setAttribute("version", "1.0");
			
			Element rootElement = doc.createElement("dict");
			pListElement.appendChild(rootElement);
			
			Element checklistTitle = doc.createElement("key");
			checklistTitle.appendChild(doc.createTextNode("Title"));
			rootElement.appendChild(checklistTitle);			
			Element checklistTitleValue = doc.createElement("string");
			checklistTitleValue.appendChild(doc.createCDATASection(checkList.getTitle() == null ? "" : checkList.getTitle()));
			rootElement.appendChild(checklistTitleValue);
						
			Element checklistId = doc.createElement("key");
			checklistId.appendChild(doc.createTextNode("FormId"));
			rootElement.appendChild(checklistId);			
			Element checklistIdVal = doc.createElement("string");
			checklistIdVal.appendChild(doc.createCDATASection(checkList.getId().toString()));
			rootElement.appendChild(checklistIdVal);
			
			Element topicKey = doc.createElement("key");
			topicKey.appendChild(doc.createTextNode("Topics"));
			rootElement.appendChild(topicKey);	
			
			Iterator<ChecklistTopic> checklistTopicItr = checkList.getCheckListTopics().iterator();
			
			Element topicArray = doc.createElement("array");
			rootElement.appendChild(topicArray);
			
			int ctr = 1;
			
			while (checklistTopicItr.hasNext())
			{
				Element topicDict = doc.createElement("dict");
				topicArray.appendChild(topicDict);
				
				ChecklistTopic checklistTopic = checklistTopicItr.next();
				
				Element topicTitle = doc.createElement("key");
				topicTitle.appendChild(doc.createTextNode("Title"));
				topicDict.appendChild(topicTitle);
				Element topicTitleVal = doc.createElement("string");
				topicTitleVal.appendChild(doc.createCDATASection(checklistTopic.getTitle()));
				topicDict.appendChild(topicTitleVal);
				
				Element topicDesc = doc.createElement("key");
				topicDesc.appendChild(doc.createTextNode("Instruction"));
				topicDict.appendChild(topicDesc);
				Element topicDescVal = doc.createElement("string");
				topicDescVal.appendChild(doc.createCDATASection(checklistTopic.getDescription() == null ? "" : checklistTopic.getDescription()));
				topicDict.appendChild(topicDescVal);
				
				Element topicSeqNo = doc.createElement("key");
				topicSeqNo.appendChild(doc.createTextNode("SequenceNumber"));
				topicDict.appendChild(topicSeqNo);
				Element topicSeqNoVal = doc.createElement("string");
				topicSeqNoVal.appendChild(doc.createCDATASection(checklistTopic.getSort_order().toString()));
				topicDict.appendChild(topicSeqNoVal);
				
				Element queKey = doc.createElement("key");
				queKey.appendChild(doc.createTextNode("Questions"));
				topicDict.appendChild(queKey);
				
				Element queArray = doc.createElement("array");
				topicDict.appendChild(queArray);
				
				Iterator<ChecklistQuestion> queItr = checklistTopic.getCheckListQuestions().iterator();
				
				while (queItr.hasNext())
				{
					
					ChecklistQuestion checklistQuestion = queItr.next();
					
					Element queDict = doc.createElement("dict");
					queArray.appendChild(queDict);
					
					Element queProblem = doc.createElement("key");
					queProblem.appendChild(doc.createTextNode("Problem"));
					queDict.appendChild(queProblem);
					Element queProblemVal = doc.createElement("string");
					queProblemVal.appendChild(doc.createCDATASection(checklistQuestion.getQuestion()));
					queDict.appendChild(queProblemVal);
					
					Element queInst = doc.createElement("key");
					queInst.appendChild(doc.createTextNode("Instruction"));
					queDict.appendChild(queInst);
					Element queInstVal = doc.createElement("string");
					queInstVal.appendChild(doc.createCDATASection(checklistQuestion.getInstruction()));
					queDict.appendChild(queInstVal);
					
					Element queIsOverall = doc.createElement("key");
					queIsOverall.appendChild(doc.createTextNode("isOverallQuestion"));
					queDict.appendChild(queIsOverall);
					Element queIsOverallVal = doc.createElement((checklistQuestion.getIsOveralQuestion() == null ? "false" : checklistQuestion.getIsOveralQuestion().toString()));					
					queDict.appendChild(queIsOverallVal);
					
					Element hasAttachment = doc.createElement("key");
					hasAttachment.appendChild(doc.createTextNode("hasAttachment"));
					queDict.appendChild(hasAttachment);
					Element hasAttachmentVal = doc.createElement("false");					
					queDict.appendChild(hasAttachmentVal);
					
					Element queSeqNo = doc.createElement("key");
					queSeqNo.appendChild(doc.createTextNode("SequenceNumber"));
					queDict.appendChild(queSeqNo);
					Element queSeqNoVal = doc.createElement("string");
					queSeqNoVal.appendChild(doc.createCDATASection(String.valueOf(ctr)));
					ctr++;
					//queSeqNoVal.appendChild(doc.createCDATASection(checklistQuestion.getSequenceNumber().toString()));
					queDict.appendChild(queSeqNoVal);
					
					Element optionKey = doc.createElement("key");
					optionKey.appendChild(doc.createTextNode("Options"));
					queDict.appendChild(optionKey);
					
					Element optionArray = doc.createElement("array");
					queDict.appendChild(optionArray);
					
					Iterator<ChecklistOption> optionIterator = checklistQuestion.getCheckListOptions().iterator();
					
					while (optionIterator.hasNext())
					{
						ChecklistOption checklistOption = optionIterator.next();
						
						Element optionDict = doc.createElement("dict");
						optionArray.appendChild(optionDict);
						
						Element optionTitle = doc.createElement("key");
						optionTitle.appendChild(doc.createTextNode("Title"));
						optionDict.appendChild(optionTitle);
						Element optionTitleVal = doc.createElement("string");
						optionTitleVal.appendChild(doc.createCDATASection(checklistOption.getOptionName()));
						optionDict.appendChild(optionTitleVal);
						
						Element optionSubTitle = doc.createElement("key");
						optionSubTitle.appendChild(doc.createTextNode("Subtitle"));
						optionDict.appendChild(optionSubTitle);
						Element optionSubTitleVal = doc.createElement("string");
						optionSubTitleVal.appendChild(doc.createCDATASection(checklistOption.getName() == null ? "" : checklistOption.getName()));
						optionDict.appendChild(optionSubTitleVal);
						
						Element optionValue = doc.createElement("key");
						optionValue.appendChild(doc.createTextNode("Value"));
						optionDict.appendChild(optionValue);
						Element optionValueVal = doc.createElement("real");
						optionValueVal.appendChild(doc.createTextNode(checklistOption.getValue() == null ? "0" : checklistOption.getValue()));
						optionDict.appendChild(optionValueVal);
						
						Element criCountValue = doc.createElement("key");
						criCountValue.appendChild(doc.createTextNode("criteriaCount"));
						optionDict.appendChild(criCountValue);
						Element criCountValueVal = doc.createElement("real");
						criCountValueVal.appendChild(doc.createTextNode((checklistOption.getCriteriaCount() == null ? "0" : checklistOption.getCriteriaCount().toString())));
						optionDict.appendChild(criCountValueVal);
						
						Element optionSeqNo = doc.createElement("key");
						optionSeqNo.appendChild(doc.createTextNode("SequenceNumber"));
						optionDict.appendChild(optionSeqNo);
						Element optionSeqNoVal = doc.createElement("string");
						optionSeqNoVal.appendChild(doc.createCDATASection(checklistOption.getSequenceNumber().toString()));
						optionDict.appendChild(optionSeqNoVal);
					}
					
					Element criteriaKey = doc.createElement("key");
					criteriaKey.appendChild(doc.createTextNode("Criterias"));
					queDict.appendChild(criteriaKey);
					
					Element criteriaArray = doc.createElement("array");
					queDict.appendChild(criteriaArray);
					
					Iterator<ChecklistCriteria> criIterator = checklistQuestion.getCheckListCriterias().iterator();
					
					while (criIterator.hasNext())
					{
						ChecklistCriteria checklistCriteria = criIterator.next();
						
						Element criteriaDict = doc.createElement("dict");
						criteriaArray.appendChild(criteriaDict);
						
						Element criteriaTitle = doc.createElement("key");
						criteriaTitle.appendChild(doc.createTextNode("Title"));
						criteriaDict.appendChild(criteriaTitle);
						Element criteriaTitleVal = doc.createElement("string");
						criteriaTitleVal.appendChild(doc.createCDATASection(checklistCriteria.getCriteria()));
						criteriaDict.appendChild(criteriaTitleVal);
						
						Element criteriaSeqNo = doc.createElement("key");
						criteriaSeqNo.appendChild(doc.createTextNode("SequenceNumber"));
						criteriaDict.appendChild(criteriaSeqNo);
						Element criteriaSeqNoVal = doc.createElement("string");
						criteriaSeqNoVal.appendChild(doc.createCDATASection(checklistCriteria.getSequenceNumber().toString()));
						criteriaDict.appendChild(criteriaSeqNoVal);
					}
					
				}
				
 			}
			
			String path = "Checklist.osceform";
			writeFile(doc, os);
			
			return path;
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	public void writeFile(Document doc, OutputStream os)
	{
		try
		{			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(os);
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "Checklist.dtd");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
			transformer.transform(source, result);
		}
		catch(Exception e)
		{
			log.error(e.getMessage(),e);
		}
	}
	
}
