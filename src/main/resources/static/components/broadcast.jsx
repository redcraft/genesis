window.GenesisBroadcast = React.createClass({
	getInitialState: function() {
		return {data: []};
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
	render: function () {
		var groupNodes = this.state.data.map(function(group) {
			var hash = $.md5(group.name);
			return (
				<GenesisGroup name={group.name} key={group.name} hash={hash} />
			);
		});
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
					<div className="col-md-4"></div>
					<div className="col-md-4"></div>
				</div>
				<GenesisModalDialog createGroup={this.createGroup}/>
			</div>
		);
	}
});