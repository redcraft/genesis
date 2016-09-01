window.GenesisSet = React.createClass({
	render: function () {
		return (
			<button type="button" className="list-group-item" onClick={() => this.props.selectSet(this.props.id)}>
				Set {this.props.id}
			</button>
		);
	}
});