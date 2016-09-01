window.GenesisBroadcast = React.createClass({
	getInitialState: function() {
		return {data: [], sets: [], messages: [], group: "", set: "", message: ""};
	},
	componentDidMount: function() {
		$.get( "api/group", function( data ) {
			this.setState({data: data});
		}.bind(this));
	},
	componentDidUpdate: function() {
		jdenticon();
	},
	createGroup: function(newGroup) {
		$.post( "api/group", newGroup, function( data ) {
			this.setState({data: data});
		}.bind(this));
	},
	deleteGroup: function(name) {
		$.ajax({
			context: this,
			url: 'api/group?name=' + name,
			type: 'DELETE',
			success: function(data) {
				this.setState({data: data});
			}
		});
	},
	broadcastMessage: function () {
		$.post( "api/broadcast", {name: this.state.group, set: this.state.set, message: this.state.message}, function( data ) {
			this.setState({data: data, message: ""});
			this.state.data.forEach(function(group) {
				if (group.name == this.state.group) {
					this.setState({sets: group.broadcastSets});
					this.state.sets.forEach(function(set) {
						if (set.id == this.state.set) {
							this.setState({messages: set.messages});
						}
					}, this)
				}
			}, this)
		}.bind(this));
	},
	selectGroup: function (name) {
		this.state.data.forEach(function(group) {
			if (group.name == name) {
				this.setState({sets: group.broadcastSets, group: name});
			}
		}, this)
	},
	selectSet: function (id) {
		this.state.sets.forEach(function(set) {
			if (set.id == id) {
				this.setState({messages: set.messages, set: set.id});
			}
		}, this)
	},
	handleMessageChange: function (e) {
		this.setState({message: e.target.value});
	},
	render: function () {
		var groupNodes = this.state.data.map(function(group) {
			var hash = $.md5(group.name);
			var date = new Date(group.date).toTimeString();
			return (
				<GenesisGroup name={group.name} date={date}  key={group.name} hash={hash} deleteGroup={this.deleteGroup} selectGroup={this.selectGroup}/>
			);
		}, this);
		var setNodes = this.state.sets.map(function(set) {
			return (
				<GenesisSet key={set.id} id={set.id} selectSet={this.selectSet}/>
			);
		}, this);
		var messageNodes = this.state.messages.map(function(message) {
			var date = new Date(message.date).toTimeString();
			return (
				<GenesisMessage key={message.date} message={message.message} date={date}/>
			);
		}, this);
		return (
			<div>
				<h1>Broadcast</h1>
				<div className="row">
					<div className="col-md-4">
						<ul className="media-list">
							{groupNodes}
						</ul>
						<button type="button" className="btn btn-default" data-toggle="modal" data-target="#createGroupModal">Create Group</button>
					</div>
					<div className="col-md-4">
						<div className="list-group">
							{setNodes}
						</div>
					</div>
					<div className="col-md-4">
						<div className="list-group">
							{messageNodes}
						</div>
						<form className={"form-vertical " + (this.state.set ? 'show' : 'hidden')} role="form">
							<div className="form-group">
								<label htmlFor="broadcastMessage">Push new broadcast</label>
								<textarea id="broadcastMessage" className="form-control" rows="3" placeholder="Broadcast message" onChange={this.handleMessageChange} value={this.state.message}></textarea>
							</div>
							<button className="btn btn-default" onClick={this.broadcastMessage}>Broadcast</button>
						</form>
					</div>
				</div>
				<GenesisModalDialog createGroup={this.createGroup}/>
			</div>
		);
	}
});