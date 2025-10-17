# Docker Image Versioning Strategy

This project uses semantic versioning for Docker images published to GitHub Container Registry (ghcr.io).

## How It Works

The workflow automatically tags images based on how you push to GitHub:

### 1. Git Tag Push (Recommended for Releases)

When you push a git tag following semantic versioning (`vMAJOR.MINOR.PATCH`):

```bash
git tag v1.2.3
git push origin v1.2.3
```

**Creates these tags:**
- `ghcr.io/david-hajnal/blog-motor:1.2.3` (full version)
- `ghcr.io/david-hajnal/blog-motor:1.2` (minor version)
- `ghcr.io/david-hajnal/blog-motor:1` (major version)
- `ghcr.io/david-hajnal/blog-motor:latest` (latest stable)

### 2. Main Branch Push

When you push to the `main` branch:

```bash
git push origin main
```

**Creates these tags:**
- `ghcr.io/david-hajnal/blog-motor:sha-abc1234` (git commit SHA)
- `ghcr.io/david-hajnal/blog-motor:latest` (latest from main)

## Usage Examples

### Creating a New Release

```bash
# Make your changes and commit
git add .
git commit -m "Add new feature"

# Create a version tag
git tag v1.0.0
git push origin main
git push origin v1.0.0
```

### Pulling Specific Versions

```bash
# Pull exact version
docker pull ghcr.io/david-hajnal/blog-motor:1.2.3

# Pull latest minor version (gets 1.2.x)
docker pull ghcr.io/david-hajnal/blog-motor:1.2

# Pull latest major version (gets 1.x.x)
docker pull ghcr.io/david-hajnal/blog-motor:1

# Pull latest stable
docker pull ghcr.io/david-hajnal/blog-motor:latest

# Pull specific commit
docker pull ghcr.io/david-hajnal/blog-motor:sha-abc1234
```

## Versioning Guidelines

### When to bump versions:

- **MAJOR** (1.x.x → 2.0.0): Breaking changes, incompatible API changes
- **MINOR** (x.1.x → x.2.0): New features, backward compatible
- **PATCH** (x.x.1 → x.x.2): Bug fixes, backward compatible

## Docker Compose Example

```yaml
services:
  blog-motor:
    image: ghcr.io/david-hajnal/blog-motor:1.2.3  # Pin to specific version
    # OR
    image: ghcr.io/david-hajnal/blog-motor:1      # Auto-update within major version
    # OR
    image: ghcr.io/david-hajnal/blog-motor:latest # Always use latest (not recommended for production)
```

## Benefits

1. **Traceability**: Every version maps to a git tag
2. **Rollback**: Easy to revert to previous versions
3. **Flexibility**: Choose your update strategy (pin to exact, minor, or major)
4. **CI/CD**: Automated tagging on git tag push
5. **SHA tracking**: Development builds have commit SHA for debugging
