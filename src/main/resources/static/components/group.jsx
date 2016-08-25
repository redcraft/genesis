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
					<a href="#"><h4 className="media-heading">{this.props.name}</h4></a>
					<p>
						Created at: 2016-05-12 <br />
						<a href="">Delete</a>
					</p>
				</div>
			</li>
		);
	}
});