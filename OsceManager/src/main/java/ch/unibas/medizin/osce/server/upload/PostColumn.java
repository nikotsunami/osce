package ch.unibas.medizin.osce.server.upload;

import java.util.List;

public class PostColumn {

	private List<PostDetail> postDetailList;

	public List<PostDetail> getPostDetailList() {
		return postDetailList;
	}

	public void setPostDetailList(List<PostDetail> postDetailList) {
		this.postDetailList = postDetailList;
	}
}
