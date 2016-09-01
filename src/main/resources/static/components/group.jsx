window.GenesisGroup = React.createClass({
	render: function () {
		return (
			<li className="media">
				<a className="pull-left" href="#">
					<svg className="media-object"  width="64" height="64" data-jdenticon-hash={this.props.hash}>
						Throw away your browser
					</svg>
				</a>
				<div className="media-body">
					<a href="#"  onClick={() => this.props.selectGroup(this.props.name)}><h4 className="media-heading">{this.props.name}</h4></a>
					<p>
						Created at: {this.props.date} <br />
						<a href="" onClick={() => this.props.deleteGroup(this.props.name)}>Delete</a>
					</p>
				</div>
			</li>
		);
	}
});