window.GenesisModalDialog = React.createClass({
	getInitialState: function() {
		return {name: '', setSize: 50, setCount: 60, communitySize: 0};
	},
	componentDidMount: function() {
		$.get( "api/community", function( data ) {
			this.setState({communitySize: data.size});
		}.bind(this));
	},
	handleGroupNameChange: function(e) {
		this.setState({name: e.target.value});
	},
	handleSetSizeChange: function(e) {
		this.setState({setSize: e.target.value});
		this.setState({setCount: this.state.communitySize / e.target.value});
	},
	handleSubmit: function(e) {
		this.props.createGroup(this.state);
		this.setState({name: '', setSize: 50, setCount: 60});
	},
	render: function () {
		return (
			<div id="createGroupModal" className="modalDialog modal fade">
				<div className="modal-dialog">
					<div className="modal-content">
						<div className="modal-header">
							<button type="button" className="close" data-dismiss="modal" aria-hidden="true">×</button>
							<h4 className="modal-title">Create Group</h4>
						</div>
						<div className="modal-body">
							<form className="form-horizontal" role="form" onSubmit={this.handleSubmit}>
								<div className="form-group">
									<div className="col-sm-3">
										<label className="control-label">Total members</label>
									</div>
									<div className="col-sm-9">
										<p className="form-control-static">{this.state.communitySize}</p>
									</div>
								</div>
								<div className="form-group">
									<div className="col-sm-3">
										<label htmlFor="groupName" className="control-label">Group Name</label>
									</div>
									<div className="col-sm-9">
										<input type="text"
											   className="form-control"
											   id="groupName"
											   placeholder="Name"
											   name="name"
											   value={this.state.name}
											   onChange={this.handleGroupNameChange}
										/>
									</div>
								</div>
								<div className="form-group">
									<div className="col-sm-3">
										<label htmlFor="setSize" className="control-label">Set size</label>
									</div>
									<div className="col-sm-9">
										<input type="number"
											   className="form-control"
											   id="setSize"
											   name="setSize"
											   placeholder="Size"
											   value={this.state.setSize}
											   onChange={this.handleSetSizeChange}
										/>
									</div>
								</div>
								<div className="form-group">
									<div className="col-sm-3">
										<label className="control-label">Number of sets</label>
									</div>
									<div className="col-sm-9">
										<p className="form-control-static">{this.state.setCount}</p>
									</div>
								</div>
							</form>
						</div>
						<div className="modal-footer">
							<a className="btn btn-default" data-dismiss="modal">Close</a>
							<a className="btn btn-primary" data-dismiss="modal" onClick={this.handleSubmit}>Create</a>
						</div>
					</div>
				</div>
			</div>
		);
	}
});