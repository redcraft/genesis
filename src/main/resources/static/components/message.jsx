window.GenesisMessage = React.createClass({
	render: function () {
		return (
			<a href="#" className="list-group-item">
				<h4 className="list-group-item-heading">{this.props.date}</h4>
				<p className="list-group-item-text">{this.props.message}</p>
			</a>
		);
	}
});