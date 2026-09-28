import './ProductCard.css';

function formatDate(value) {
  if (!value) return '';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return '';
  return date.toLocaleDateString();
}

export default function ProductCard({ product }) {
  const { title, description, price, currency, measure, company, status, createdAt } = product;

  return (
    <article className="product-card">
      <div className="product-card__header">
        <h3 className="product-card__title">{title}</h3>
        {status && <span className="product-card__status">{status}</span>}
      </div>
      {description && <p className="product-card__description">{description}</p>}
      <p className="product-card__price">
        {price} {currency}
        {measure && <span className="product-card__measure"> / {measure}</span>}
      </p>
      <div className="product-card__footer">
        <span className="product-card__company">{company}</span>
        <span className="product-card__date">{formatDate(createdAt)}</span>
      </div>
    </article>
  );
}
